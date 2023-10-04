
import timer.Timer

/**
 * A rate limiter limits the rate of change of some value. This is useful for implementing
 * voltage ramps, velocity ramps and the like. For controlling position, consider using
 * a trapezoid profile instead.
 *
 * - Since this filter has memory, use a separate instance of this class for every input
 * 	 stream.
 */
class RateLimiter {
	/**
	 * The rate-of-change limit in the positive direction, in units per second.
	 * This is expected to be positive.
	 */
	var positiveRateLimit: Double = 0.0 // This value will be changed when the constructor is called
		set(value) {
			require(value > 0)
			field = value
		}
	/**
	 * The rate-of-change limit in the negative direction, in units per second.
	 * This is expected to be negative.
	 */
	var negativeRateLimit: Double = 0.0 // This value will be changed when the constructor is called
		set(value) {
			require(value < 0)
			field = value
		}
	private val timer = Timer()
	private var previousInput: Double? = null
	private var output = 0.0

	/**
	 * @param positiveRateLimit The rate-of-change limit in the positive direction,
	 * in units per second. This is expected to be positive.
	 * @param negativeRateLimit The rate-of-change limit in the negative direction,
	 * in units per second. This is expected to be negative.
	 */
	constructor(positiveRateLimit: Double, negativeRateLimit: Double) {
		this.positiveRateLimit = positiveRateLimit
		this.negativeRateLimit = negativeRateLimit
	}

	/**
	 * Calculates the rate-limited output based on the original input.
	 * This function should be called periodically.
	 */
	fun calculate(input: Double): Double {
		if(previousInput == null) { // Will only be null on first call to calculate
			this.reset(input)
		}
		val secondsSinceLastCall = timer.seconds
		timer.reset()

		val changeInInput = input - previousInput!!
		previousInput = input

		val maxChangeInOutput = positiveRateLimit * secondsSinceLastCall
		val minChangeInOutput = negativeRateLimit * secondsSinceLastCall
		val changeInOutput = changeInInput.coerceIn(minChangeInOutput, maxChangeInOutput)
		output += changeInOutput

		return output
	}

	/**
	 * Sets the rate limiter to a new value, ignoring the rate limit while doing so.
	 */
	fun reset(newValue: Double) {
		previousInput = newValue
		output = newValue
		timer.resetAndStart()
	}
}