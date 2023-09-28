package timer

/**
 * A modified version of WPILib's `Timer.java` in `edu.wpi.first.wpilibj`,
 * that is both RoboRIO and PC friendly (which is useful for tests, or just
 * for code that isn't for the robot).
 */
class Timer() {
    private var startTimeMillis: Long? = null
    private var accumulatedTimeMillis = 0L
    var running = false
        private set

    var millis = 0L
        get() {
            if(startTimeMillis == null) return 0L
            return if(running) {
                (System.currentTimeMillis() - startTimeMillis!!) + accumulatedTimeMillis
            } else accumulatedTimeMillis
        }
        private set

    var seconds = 0.0
        get() = (millis / 1000).toDouble()
        private set

    /** Starts the timer if it isn't running already. */
    fun start() {
        if(!running) {
            startTimeMillis = System.currentTimeMillis()
            running = true
        }
    }

    /**
     * Resets the time to 0 by setting the accumulated
     * time to 0 and the start time to the current time.
     * Does NOT stop the timer if it's running.
     */
    fun reset() {
        startTimeMillis = System.currentTimeMillis()
        accumulatedTimeMillis = 0L
    }

    fun resetAndStart() {
        reset()
        start()
    }

    fun stopAndReset() {
        stop()
        reset()
    }

    /**
     * Stops the timer. Calls to getMillis or getSeconds will return the time as of now.
     */
    fun stop() {
        if(running) {
            accumulatedTimeMillis = millis
            running = false
        }
    }

    /** Returns true if the specified milliseconds have passed, and false otherwise. */
    fun hasElapsed(millis: Number) = this.millis >= millis.toDouble()
}
