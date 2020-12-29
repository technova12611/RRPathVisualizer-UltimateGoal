import com.acmerobotics.roadrunner.geometry.Pose2d
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

import javafx.stage.Stage
import javafx.util.Duration


class App : Application() {

    val isBlueAlliance = true

    val robotRect = Rectangle(100.0, 100.0, 10.0, 10.0)
    val startRect = Rectangle(100.0, 100.0, 10.0, 10.0)
    val endRect = Rectangle(100.0, 100.0, 10.0, 10.0)

    var startTime = Double.NaN

    var trajectories = if(isBlueAlliance) TrajectoryGenBlue.createTrajectory() else TrajectoryGen.createTrajectory()

    lateinit var fieldImage: Image
    lateinit var stage: Stage

    var activeTrajectoryIndex = 0
    val trajectoryDurations = trajectories.map { it.duration() }
    var duration = trajectoryDurations.sum()
    var numberOfTrajectories = trajectories.size
    var firstTime = true
    var nextMarker = -1
    var elapsedTime = 0.0

    companion object {
        var WIDTH = 0.0
        var HEIGHT = 0.0
    }

    override fun start(stage: Stage?) {
        this.stage = stage!!
        fieldImage = Image("FTC_Ultimate_Goal_717x717_dark_Field.png")

//        val imageView = ImageView(fieldImage)
//        imageView.setPreserveRatio(false)
//        imageView.setFitWidth(750.0)
//        imageView.setFitHeight(750.0)
//        fieldImage = imageView.snapshot(null, null)

        val root = Group()

        WIDTH = fieldImage.width
        HEIGHT = fieldImage.height
        GraphicsUtil.pixelsPerInch = WIDTH / GraphicsUtil.FIELD_WIDTH
        GraphicsUtil.halfFieldPixels = WIDTH / 2.0

        val canvas = Canvas(WIDTH, HEIGHT)
        val gc = canvas.graphicsContext2D
        val t1 = Timeline(KeyFrame(Duration.millis(10.0), EventHandler<ActionEvent> { run(gc) }))
        t1.cycleCount = Timeline.INDEFINITE

        stage.scene = Scene(
            StackPane(
                root
            )
        )

        val text = Text(0.0,0.0, "This is a test")
        text.stroke = Color.GREEN
        root.children.addAll(canvas, startRect, endRect, robotRect)

        stage.title = "Team 12611 PathVisualizer"
        stage.isResizable = false

        var runningTotal=0.0
        trajectoryDurations.forEachIndexed { index,element -> runningTotal +=element; println(" ${index+1} duration: ${"%.3f".format(element)}  total: ${"%.3f".format(runningTotal)}")}

        println("total duration ${"%.3f".format(duration)}")

        stage.show()
        t1.play()
    }

    fun run(gc: GraphicsContext) {

        GraphicsUtil.gc = gc
        gc.drawImage(fieldImage, 0.0, 0.0)

        gc.lineWidth = GraphicsUtil.LINE_THICKNESS

        gc.globalAlpha = 0.5
        GraphicsUtil.setColor(Color.RED)
        if(isBlueAlliance) {
            TrajectoryGenBlue.drawOffbounds1()
            TrajectoryGenBlue.drawOffbounds2()
        } else {
            TrajectoryGen.drawOffbounds1()
            TrajectoryGen.drawOffbounds2()
            //TrajectoryGen.drawOffbounds3()
        }
        gc.globalAlpha = 1.0

        val trajectory = trajectories[activeTrajectoryIndex]

        if(firstTime) {
            firstTime = false
            if(trajectory.markers.isNotEmpty()) {
                nextMarker = 0
                elapsedTime = 0.0
            }
        }

        val prevDurations: Double = {
            var x = 0.0
            for (i in 0 until activeTrajectoryIndex)
                x += trajectoryDurations[i]
            x
        }()

        if (startTime.isNaN()) {
            startTime = Clock.seconds
        }

        val time = Clock.seconds
        val profileTime = time - startTime - prevDurations
        val profile_duration = trajectoryDurations[activeTrajectoryIndex]

        val start = trajectories.first().start()
        val end = trajectories.last().end()
        val current = trajectory[profileTime]

        if(trajectory.markers.isNotEmpty()) {

            if(nextMarker >-1 && trajectory.markers[nextMarker].time <= profileTime) {
                println("maker time: ${"%.3f".format(trajectory.markers[nextMarker].time)} | profileTime:${"%.3f".format(profileTime)}")
                trajectory.markers[nextMarker].callback.onMarkerReached()
                if(nextMarker+1 >= trajectory.markers.size) {
                    nextMarker = -1
                } else {
                    nextMarker++
                }
            }

        }

        if (profileTime >= profile_duration) {
            activeTrajectoryIndex++
            if(activeTrajectoryIndex >= numberOfTrajectories) {
                activeTrajectoryIndex = 0
                startTime = time
                elapsedTime = 0.0
            }
            if(trajectories[activeTrajectoryIndex].markers.isNotEmpty()) {
                nextMarker = 0
            }

            if(numberOfTrajectories > 1 && activeTrajectoryIndex != 0) {
                elapsedTime += profile_duration
            }
        }


        trajectories.forEach{
            GraphicsUtil.drawSampledPath(it.path)
        }

        GraphicsUtil.updateRobotRect(startRect, start, GraphicsUtil.END_BOX_COLOR, 0.5)
        if(isBlueAlliance) {
            GraphicsUtil.updateRobotRect(
                endRect,
                Pose2d(5.0, 40.0, 0.0.toRadians),
                GraphicsUtil.ROBOT_VECTOR_COLOR,
                0.2
            )
        } else {
            GraphicsUtil.updateRobotRect(
                endRect,
                Pose2d(5.0, -40.0, 0.0.toRadians),
                GraphicsUtil.ROBOT_VECTOR_COLOR,
                0.2
            )
        }

        GraphicsUtil.updateRobotRect(robotRect, current, GraphicsUtil.ROBOT_COLOR, 0.75)
        GraphicsUtil.drawRobotVector(current)

        stage.title = "Technova - Profile ${activeTrajectoryIndex+1} duration : ${"%.3f".format(profile_duration)} - time in profile ${"%.3f".format(elapsedTime+profileTime)} - total ${"%.3f".format(duration)}"
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}