package com.glyphsynapse.app.domain.animation

import android.content.Context

object AnimationRegistry {
    private val staticAnimations: List<AnimationDefinition> = listOf(
        BreatheAnimation, // Now Vitality
        PulseWaveAnimation, // Now Synapse
        IdleDriftAnimation, // Now Presence
        CascadeAnimation,
        OrbitAnimation,
        HeartbeatAnimation,
        MatrixRainAnimation,
    )

    fun all(context: Context): List<AnimationDefinition> =
        staticAnimations + ChargingFillAnimation(context)

    fun find(name: String, context: Context): AnimationDefinition =
        all(context).firstOrNull { it.name == name } ?: BreatheAnimation
}
