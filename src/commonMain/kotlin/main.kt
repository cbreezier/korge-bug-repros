import com.soywiz.klock.seconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.Korge
import com.soywiz.korge.time.delay
import com.soywiz.korge.time.frameBlock
import com.soywiz.korge.view.anchor
import com.soywiz.korge.view.filter.ColorMatrixFilter
import com.soywiz.korge.view.image
import com.soywiz.korge.view.position
import com.soywiz.korge.view.scale
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.random.Random

suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {
	val bitmap = resourcesVfs["korge.png"].readBitmap()
	val images = (0 until 30).map {
		image(bitmap) {
			anchor(.5, .5)
			scale(.05)
			position(64 + 24 * it, 64 + 24 * it)
		}
	}

	// Show FPS overlay
	views.debugViews = true

	// Main render loop
	launchImmediately {
		frameBlock(144.timesPerSecond) {
			while (true) {
				frame()
			}
		}
	}

	// Update filters every so often
	launch {
		while (true) {
			delay(2.seconds)
			for (image in images) {
				image.filter = if (Random.nextBoolean()) {
					ColorMatrixFilter(ColorMatrixFilter.SEPIA_MATRIX)
				} else {
					ColorMatrixFilter(ColorMatrixFilter.GRAYSCALE_MATRIX)
				}
			}
		}
	}
}