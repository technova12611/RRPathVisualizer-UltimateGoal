import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.*
import java.util.*
import kotlin.collections.ArrayList

object TrajectoryGenBlue {
    private val accelConstraint = ProfileAccelerationConstraint(45.0);
    private val constraints = MinVelocityConstraint(
        Arrays.asList(
            AngularVelocityConstraint(180.0.toRadians),
            MecanumVelocityConstraint(45.0, 13.5)
        ))

    fun createTrajectory(): ArrayList<Trajectory> {
        return testUltimateGoalPath()
    }
    /**
     * This is a demo to showcase different path builder functions
     * Road Runner Coordinates:
     *              (+x)
     *               |
     *               |
     *               |
     *               |
     * (+y) -------(0,0)--- o (Robot) --- (-y)
     *               |
     *               |
     *               |
     *               |
     *               |
     *              -x
     */

    fun testUltimateGoalPath(): ArrayList<Trajectory> {

        val list = ArrayList<Trajectory>()
        var builder0 = TrajectoryBuilder(Pose2d(-63.0, 15.0, 0.0.toRadians), 0.0.toRadians,constraints, accelConstraint)

        builder0.splineTo(Vector2d(-36.0,15.0), 0.0.toRadians )
        //                .splineTo(Vector2d(0.0,-34.0), 0.0.toRadians )
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(-36.0, 15.0, 0.0.toRadians), 0.0.toRadians,constraints, accelConstraint)
        builder0.lineTo(Vector2d(-36.0,36.0))
        list.add(builder0.build())

        // drop first wobble
        builder0 = TrajectoryBuilder(Pose2d(-36.0, 36.0, 0.0.toRadians), 0.0.toRadians,constraints, accelConstraint)
//        builder0.lineToLinearHeading(Pose2d(54.0, -45.0, 0.0.toRadians))
        builder0.splineTo(Vector2d(61.0, 48.0), 0.0.toRadians)
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(61.0, 48.0,0.0.toRadians),true, constraints, accelConstraint)
        builder0.lineToLinearHeading(Pose2d(-33.0, 40.0, 0.0.toRadians))
        list.add(builder0.build())

        // drop second wobble
        builder0 = TrajectoryBuilder(Pose2d(-33.0, 40.0,0.0.toRadians), 0.0,constraints, accelConstraint)
        builder0.splineToLinearHeading(Pose2d(56.0, 48.0, (-90.0).toRadians), 0.0)
        list.add(builder0.build())


        builder0 = TrajectoryBuilder(Pose2d(56.0, 48.0, (-90.0).toRadians),(-90.0).toRadians, constraints, accelConstraint)
        builder0.splineTo(Vector2d(32.0, 2.0), (-90.0).toRadians)
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(32.0, 2.0, (90.0).toRadians), true,constraints, accelConstraint)
        builder0.splineToLinearHeading(Pose2d(-8.0, 36.0, (0.0).toRadians), 180.0.toRadians)
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(-8.0, 36.0, 0.0.toRadians),0.0.toRadians, constraints, accelConstraint)
        builder0.lineTo(Vector2d(12.0, 36.0))
        list.add(builder0.build())

        return list
    }

    fun drawOffbounds1() {
        GraphicsUtil.fillShootingPosition(Vector2d(-36.0,15.0))
        GraphicsUtil.fillShootingPosition(Vector2d(-36.0,36.0))
        GraphicsUtil.fillShootingPosition(Vector2d(-8.0,36.0))

        GraphicsUtil.fillRect(Vector2d(12.0, 36.0), 18.0, 18.0, false) // robot against the wall
    }

    fun drawOffbounds2() {
        // draw ring and wobble initial positions
        GraphicsUtil.fillRing(Vector2d(-24.0, 36.0), 2.5)
        GraphicsUtil.fillWobbleInStarting(Vector2d(-48.0, 48.0), 4.0)

        // draw wobble in dropping zone
        GraphicsUtil.fillWobbleInDropZone(Vector2d(61.0, 57.0), 4.0)
        GraphicsUtil.fillWobbleInDropZone(Vector2d(56.0, 65.0), 4.0)

        // draw bounced back rings
        GraphicsUtil.fillRing(Vector2d(30.0, -10.0), 2.5)
        GraphicsUtil.fillRing(Vector2d(48.0, 12.0), 2.5)
    }

    val Double.toRadians get() = (Math.toRadians(this))
}
