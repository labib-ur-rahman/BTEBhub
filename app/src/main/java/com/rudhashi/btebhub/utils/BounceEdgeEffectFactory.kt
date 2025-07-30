package com.rudhashi.btebhub.utils

import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView

class BounceEdgeEffectFactory : RecyclerView.EdgeEffectFactory() {
    override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int): EdgeEffect {
        return object : EdgeEffect(recyclerView.context) {
            override fun onPull(deltaDistance: Float, displacement: Float) {
                super.onPull(deltaDistance, displacement)
                recyclerView.translationY += deltaDistance * recyclerView.height
            }

            override fun onRelease() {
                super.onRelease()
                recyclerView.animate().translationY(0f).setDuration(300).start()
            }

            override fun onAbsorb(velocity: Int) {
                super.onAbsorb(velocity)
                recyclerView.animate().translationY(0f).setDuration(300).start()
            }
        }
    }
}
