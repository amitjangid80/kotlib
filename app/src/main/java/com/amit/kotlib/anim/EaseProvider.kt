package com.amit.kotlib.anim

/**
 * Created by Amit Jangid on 21,May,2018
 *
 * The Easing class provides a collection of ease functions. It does not use the standard 4 param
 * ease signature. Instead it uses a single param which indicates the current linear ratio (0 to 1) of the tween.
**/
@Suppress("NAME_SHADOWING")
internal object EaseProvider
{
    /**
     * get method
     *
     * @param ease              Easing type
     * @param elapsedTimeRate   Elapsed time / Total time
     *
     * @return easedValue
    **/
    operator fun get(ease: Ease, elapsedTimeRate: Float): Float
    {
        var elapsedTimeRate = elapsedTimeRate

        when (ease)
        {
            Ease.LINEAR -> return elapsedTimeRate

            Ease.QUAD_IN -> return getPowIn(elapsedTimeRate, 2.0)

            Ease.QUAD_OUT -> return getPowOut(elapsedTimeRate, 2.0)

            Ease.QUAD_IN_OUT -> return getPowInOut(elapsedTimeRate, 2.0)

            Ease.CUBIC_IN -> return getPowIn(elapsedTimeRate, 3.0)

            Ease.CUBIC_OUT -> return getPowOut(elapsedTimeRate, 3.0)

            Ease.CUBIC_IN_OUT -> return getPowInOut(elapsedTimeRate, 3.0)

            Ease.QUART_IN -> return getPowIn(elapsedTimeRate, 4.0)

            Ease.QUART_OUT -> return getPowOut(elapsedTimeRate, 4.0)

            Ease.QUART_IN_OUT -> return getPowInOut(elapsedTimeRate, 4.0)

            Ease.QUINT_IN -> return getPowIn(elapsedTimeRate, 5.0)

            Ease.QUINT_OUT -> return getPowOut(elapsedTimeRate, 5.0)

            Ease.QUINT_IN_OUT -> return getPowInOut(elapsedTimeRate, 5.0)

            Ease.SINE_IN -> return (1f - Math.cos(elapsedTimeRate * Math.PI / 2f)).toFloat()

            Ease.SINE_OUT -> return Math.sin(elapsedTimeRate * Math.PI / 2f).toFloat()

            Ease.SINE_IN_OUT -> return (-0.5f * (Math.cos(Math.PI * elapsedTimeRate) - 1f)).toFloat()

            Ease.BACK_IN -> return (elapsedTimeRate.toDouble() * elapsedTimeRate.toDouble() * ((1.7 + 1f) * elapsedTimeRate - 1.7)).toFloat()

            Ease.BACK_OUT -> return ((--elapsedTimeRate).toDouble() * elapsedTimeRate.toDouble() * ((1.7 + 1f) * elapsedTimeRate + 1.7) + 1f).toFloat()

            Ease.BACK_IN_OUT -> return getBackInOut(elapsedTimeRate, 1.7f)

            Ease.CIRC_IN -> return (-(Math.sqrt((1f - elapsedTimeRate * elapsedTimeRate).toDouble()) - 1)).toFloat()

            Ease.CIRC_OUT -> return Math.sqrt((1f - --elapsedTimeRate * elapsedTimeRate).toDouble()).toFloat()

            Ease.CIRC_IN_OUT ->
            {
                // {line = bufferedReader.readLine(); line}()
                return if ({ elapsedTimeRate *= 2f; elapsedTimeRate }() < 1f)
                {
                    (-0.5f * (Math.sqrt((1f - elapsedTimeRate * elapsedTimeRate).toDouble()) - 1f)).toFloat()
                }
                else (0.5f * (Math.sqrt((1f - { elapsedTimeRate -= 2f; elapsedTimeRate }() * elapsedTimeRate).toDouble()) + 1f)).toFloat()
            }

            Ease.BOUNCE_IN -> return getBounceIn(elapsedTimeRate)

            Ease.BOUNCE_OUT -> return getBounceOut(elapsedTimeRate)

            Ease.BOUNCE_IN_OUT ->
            {
                return if (elapsedTimeRate < 0.5f)
                {
                    getBounceIn(elapsedTimeRate * 2f) * 0.5f
                }
                else getBounceOut(elapsedTimeRate * 2f - 1f) * 0.5f + 0.5f
            }

            Ease.ELASTIC_IN -> return getElasticIn(elapsedTimeRate, 1.0, 0.3)

            Ease.ELASTIC_OUT -> return getElasticOut(elapsedTimeRate, 1.0, 0.3)

            Ease.ELASTIC_IN_OUT -> return getElasticInOut(elapsedTimeRate, 1.0, 0.45)

            else -> return elapsedTimeRate
        }
    }

    /**
     * get pow in method
     *
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
    **/
    private fun getPowIn(elapsedTimeRate: Float, pow: Double): Float
    {
        return Math.pow(elapsedTimeRate.toDouble(), pow).toFloat()
    }

    /**
     * get pow out method
     *
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
    **/
    private fun getPowOut(elapsedTimeRate: Float, pow: Double): Float
    {
        return (1.toFloat() - Math.pow((1 - elapsedTimeRate).toDouble(), pow)).toFloat()
    }

    /**
     * get pow in out
     *
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
    **/
    private fun getPowInOut(elapsedTimeRate: Float, pow: Double): Float
    {
        var elapsedTimeRate = elapsedTimeRate

        return if ({ elapsedTimeRate *= 2f; elapsedTimeRate }() < 1)
        {
            (0.5 * Math.pow(elapsedTimeRate.toDouble(), pow)).toFloat()
        }
        else (1 - 0.5 * Math.abs(Math.pow((2 - elapsedTimeRate).toDouble(), pow))).toFloat()
    }

    /**
     * get back in out method
     *
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amount          amount The strength of the ease.
     * @return easedValue
    **/
    private fun getBackInOut(elapsedTimeRate: Float, amount: Float): Float
    {
        var elapsedTimeRate = elapsedTimeRate
        var amount = amount
        amount *= 1.525f

        return if ({ elapsedTimeRate *= 2f; elapsedTimeRate }() < 1)
        {
            (0.5 * (elapsedTimeRate * elapsedTimeRate * ((amount + 1) * elapsedTimeRate - amount))).toFloat()
        }
        else (0.5 * ({ elapsedTimeRate -= 2f; elapsedTimeRate }() * elapsedTimeRate * ((amount + 1) * elapsedTimeRate + amount) + 2)).toFloat()
    }

    /**
     * get bounce in method
     *
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private fun getBounceIn(elapsedTimeRate: Float): Float {
        return 1f - getBounceOut(1f - elapsedTimeRate)
    }

    /**
     * get bounce out method
     *
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private fun getBounceOut(elapsedTimeRate: Float): Float {
        var elapsedTimeRate = elapsedTimeRate

        return when
        {
            elapsedTimeRate < 1 / 2.75 -> (7.5625 * elapsedTimeRate.toDouble() * elapsedTimeRate.toDouble()).toFloat()
            elapsedTimeRate < 2 / 2.75 -> (7.5625 * { elapsedTimeRate -= (1.5 / 2.75).toFloat(); elapsedTimeRate }().toDouble() * elapsedTimeRate.toDouble() + 0.75).toFloat()
            elapsedTimeRate < 2.5 / 2.75 -> (7.5625 * { elapsedTimeRate -= (2.25 / 2.75).toFloat(); elapsedTimeRate }().toDouble() * elapsedTimeRate.toDouble() + 0.9375).toFloat()
            else -> (7.5625 * { elapsedTimeRate -= (2.625 / 2.75).toFloat(); elapsedTimeRate }().toDouble() * elapsedTimeRate.toDouble() + 0.984375).toFloat()
        }
    }

    /**
     * get elastic in
     *
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
    **/
    private fun getElasticIn(elapsedTimeRate: Float, amplitude: Double, period: Double): Float
    {
        var elapsedTimeRate = elapsedTimeRate

        if (elapsedTimeRate == 0f || elapsedTimeRate == 1f)
        {
            return elapsedTimeRate
        }

        val pi2 = Math.PI * 2
        val s = period / pi2 * Math.asin(1 / amplitude)
        return (-(amplitude * Math.pow(2.0, (10f * { elapsedTimeRate -= 1f; elapsedTimeRate }()).toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period))).toFloat()
    }

    /**
     * get elastic out
     *
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
    **/
    private fun getElasticOut(elapsedTimeRate: Float, amplitude: Double, period: Double): Float
    {
        if (elapsedTimeRate == 0f || elapsedTimeRate == 1f)
        {
            return elapsedTimeRate
        }

        val pi2 = Math.PI * 2
        val s = period / pi2 * Math.asin(1 / amplitude)
        return (amplitude * Math.pow(2.0, (-10 * elapsedTimeRate).toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period) + 1).toFloat()
    }

    /**
     * get elastic in out
     *
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
    **/
    private fun getElasticInOut(elapsedTimeRate: Float, amplitude: Double, period: Double): Float
    {
        var elapsedTimeRate = elapsedTimeRate
        val pi2 = Math.PI * 2
        val s = period / pi2 * Math.asin(1 / amplitude)

        return if ({ elapsedTimeRate *= 2f; elapsedTimeRate }() < 1)
        {
            (-0.5f * (amplitude * Math.pow(2.0, (10 * { elapsedTimeRate -= 1f; elapsedTimeRate }().toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period))).toFloat())
        }
        else (amplitude * Math.pow(2.0, (-10 * { elapsedTimeRate -= 1f; elapsedTimeRate }()).toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period) * 0.5 + 1).toFloat()
    }
}
