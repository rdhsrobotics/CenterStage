package org.robotics.robotics.xdk.teamcode.autonomous.purepursuit;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Point;
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class PurePursuitPath {
    private LinkedList<FieldWaypoint> waypoints = new LinkedList<>();
    private LinkedList<ActionWaypoint> actionWaypoints = new LinkedList<>();
    private int targetIdx = 1;
    private boolean finished;

    public PurePursuitPath(WaypointLike... ws) {
        if (ws.length < 2) throw new IllegalArgumentException();
        waypoints.addAll(
                Arrays.stream(ws)
                        .filter(waypointLike -> waypointLike instanceof FieldWaypoint)
                        .map(waypointLike -> (FieldWaypoint) waypointLike)
                        .collect(Collectors.toList())
        );

        actionWaypoints.addAll(
                WaypointFilterUtilsKt.populateAndExtractActions(Arrays.asList(ws))
        );

        if (waypoints.getLast().getType() != FieldWaypoint.Type.POSE)
            throw new IllegalArgumentException("Last waypoint is not a pose");
    }

    public Pose calculateTargetPose(Pose robot) {
        FieldWaypoint prev = waypoints.get(targetIdx - 1);

        final ActionWaypoint incomplete = WaypointFilterUtilsKt.findWithIndexIncomplete(actionWaypoints, prev.id);
        if (incomplete != null) {
            ForkJoinPool.commonPool().execute(() -> {
                incomplete.getAction().invoke();
            });
            incomplete.setHasExecuted(true);
        }

        FieldWaypoint target = waypoints.get(targetIdx);

        double distance = robot.distanceTo(target.getPoint());

        if (distance > target.getRadius()) {
            Point intersection = PurePursuitUtil.lineCircleIntersection(
                    prev.getPoint(), target.getPoint(), robot, target.getRadius());
            Pose targetPose;

            if (target.getType() == FieldWaypoint.Type.POSE) {
                targetPose = new Pose(intersection, ((Pose) target.getPoint()).heading);
            } else {
                double robotAngle = AngleUnit.normalizeRadians(robot.heading);
                double forwardAngle = intersection.subtract(robot).atan() - (Math.PI / 2);
                double backwardsAngle = AngleUnit.normalizeRadians(forwardAngle + Math.PI);

                double autoAngle =
                        Math.abs(AngleUnit.normalizeRadians(robotAngle - forwardAngle)) <
                                Math.abs(AngleUnit.normalizeRadians(robotAngle - backwardsAngle)) ?
                                forwardAngle : backwardsAngle;

                targetPose = new Pose(intersection, autoAngle);
            }

            return targetPose;
        } else {
            if (targetIdx == waypoints.size() - 1) {
                finished = true;
                return getEndPose();
            } else {
                targetIdx++;
                return calculateTargetPose(robot);
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public Pose getEndPose() {
        return (Pose) waypoints.getLast().getPoint();
    }

    public double getRadius() {
        return waypoints.get(targetIdx).getRadius();
    }
}