package ui.anwesome.com.plusfarview

/**
 * Created by anweshmishra on 21/03/18.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
class PlusFarView(ctx : Context) : View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class State(var j : Int = 0, var dir : Int = 0, var prevScale : Float = 0f) {
        val scales : Array<Float> = arrayOf(0f, 0f)
        fun update(stopcb : (Float) -> Unit) {
            scales[j] += dir * 0.1f
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir
                if (j == scales.size || j == -1) {
                    j -= dir
                    dir = 0
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0) {
                dir = 1 - 2 * prevScale.toInt()
                startcb()
            }
        }
    }
    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }
        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }
    data class PlusFar(var i : Int, val state : State = State()) {
        fun draw(canvas : Canvas, paint : Paint) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            val size = Math.min(w,h)/10
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.WHITE
            paint.strokeWidth = size / 5
            for (i in 0..1) {
                canvas.save()
                canvas.translate(w/2, h/2)
                canvas.rotate(i * 90f * state.scales[1])
                for(j in 0..1) {
                    canvas.save()
                    canvas.scale(1f - 2* j, 1f)
                    val x = (w/2 + paint.strokeWidth) * (1 - state.scales[0])
                    canvas.drawLine(x, 0f, x + size, 0f, paint)
                    canvas.restore()
                }
                canvas.restore()
            }
        }
        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view : PlusFarView) {
        val plusFar : PlusFar = PlusFar(0)
        val animator : Animator = Animator(view)
        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            plusFar.draw(canvas, paint)
            animator.animate {
                plusFar.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            plusFar.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity : Activity) : PlusFarView {
            val view = PlusFarView(activity)
            activity.setContentView(view)
            return view
        }
    }
}