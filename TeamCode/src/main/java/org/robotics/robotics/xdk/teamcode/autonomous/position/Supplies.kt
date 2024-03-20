package org.robotics.robotics.xdk.teamcode.autonomous.position

interface Supplies<I, O>
{
    fun supply(input: I): O
}