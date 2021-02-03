import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint
import java.util.*
import kotlin.collections.ArrayList

object TrajectoryGen {

    private val accelConstraint = ProfileAccelerationConstraint(60.0);
    private val constraints = MinVelocityConstraint(
        Arrays.asList(
         AngularVelocityConstraint(180.0.toRadians),
         MecanumVelocityConstraint(60.0, 13.5)
    ))

    fun createTrajectory(): ArrayList<Trajectory> {
        return testUltimateGoalPath()
    }

    fun testUltimateGoalPath(): ArrayList<Trajectory> {

        val list = ArrayList<Trajectory>()
        var builder0 = TrajectoryBuilder(Pose2d(-63.0, -15.0, 0.0.toRadians), 0.0.toRadians,constraints, accelConstraint)

        builder0.splineTo(Vector2d(-36.0,-15.0), 0.0.toRadians )
        //                .splineTo(Vector2d(0.0,-34.0), 0.0.toRadians )
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(-36.0, -15.0, 0.0.toRadians), 0.0.toRadians,constraints, accelConstraint)
        builder0.lineTo(Vector2d(-36.0,-36.0))
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(-36.0, -36.0, 0.0.toRadians), 0.0.toRadians,constraints, accelConstraint)
//        builder0.lineToLinearHeading(Pose2d(54.0, -45.0, 0.0.toRadians))
        builder0.splineTo(Vector2d(61.0, -42.0), 0.0.toRadians)
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(61.0, -42.0,0.0.toRadians), true,constraints, accelConstraint)
        builder0.lineToLinearHeading(Pose2d(-33.0, -40.0, 0.0.toRadians))
        list.add(builder0.build())

//        val callabck = MarkerCallback { println("test marker 0....")}

        builder0 = TrajectoryBuilder(Pose2d(-33.0, -40.0,0.0.toRadians), true,constraints, accelConstraint)
        builder0.lineToLinearHeading(Pose2d(55.0, -48.0, 0.0))
          .addTemporalMarker(1.0, MarkerCallback{ println("test marker 0....")})
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(55.0, -48.0,0.0.toRadians), true,constraints, accelConstraint)
        builder0.splineTo(Vector2d(32.0, -2.0), (180.0 - 90.0).toRadians)
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(32.0, -2.0, (-90.0).toRadians), true,constraints, accelConstraint)
        builder0.splineToLinearHeading(Pose2d(-8.0, -36.0, (0.0).toRadians), 180.0.toRadians)
        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(-8.0, -36.0, 0.0.toRadians), true,constraints, accelConstraint)
        builder0.lineTo(Vector2d(12.0, -36.0))
        list.add(builder0.build())

        return list
    }

    fun testHeadingInterpolators(): ArrayList<Trajectory> {

        val list = ArrayList<Trajectory>()
        var builder0 = TrajectoryBuilder(Pose2d(56.5, -39.0, 90.0.toRadians), 90.0.toRadians,constraints, accelConstraint)
        builder0.lineToLinearHeading(Pose2d(56.5, -30.0, 90.0.toRadians))

        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(56.5, -30.0,  90.0.toRadians), 90.0.toRadians,constraints, accelConstraint)
        builder0.splineToLinearHeading(Pose2d(40.0, -55.0, 90.0.toRadians),  (90.0).toRadians)
        builder0.splineTo(Vector2d(0.0, -42.0),  (-180.0).toRadians)

        list.add(builder0.build())

        return list
    }

    fun testHeadingInterpolators2(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()
        val startPose = Pose2d(-33.0, -63.0, 180.0.toRadians)

        var builder1 = TrajectoryBuilder(startPose, 180.0.toRadians,constraints, accelConstraint)

        builder1
            .strafeTo( Vector2d(-60.0, -40.0))
            //.addSpatialMarker(Vector2d(-50.0, -30.0), MarkerCallback{println("spatial marker triggered ...")} )
            //.addDisplacementMarker ( MarkerCallback {println("grab stone 1") })

        // 1. get first stone
        list.add(builder1.build())

        builder1 = TrajectoryBuilder(Pose2d(-60.0, -40.0, 180.0.toRadians),true,constraints, accelConstraint)

        builder1
            .splineTo(Vector2d(0.0, -42.0), 0.0.toRadians)
            .splineTo(Vector2d(30.0, -39.5), 0.0.toRadians)
            .splineTo(Vector2d(49.0,-39.5), 0.0.toRadians)
            //.lineToConstantHeading(Vector2d(0.0,-42.5))
            //.lineToConstantHeading(Vector2d(30.0,-40.5))
            //.lineToConstantHeading(Vector2d(62.0,-40.0))
            //.addDisplacementMarker ( MarkerCallback { println("release stone 1") })


        // 2. place first stone
        list.add(builder1.build())

        return list
    }

    fun testSplineTurn3(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()
        val myPose = Pose2d(-59.0, -35.0, 0.0)
        var builder0 = TrajectoryBuilder(myPose, Math.PI,constraints, accelConstraint)
        builder0.strafeTo(Vector2d(-59.0,-38.0))
            .splineTo(Vector2d(0.0,-41.0), Math.PI +  Math.PI)
            .splineTo(Vector2d(55.0, -45.0), (Math.PI+Math.PI/2))

        list.add(builder0.build())

        return list
    }

    fun testSplineTurn2(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()
        val myPose = Pose2d(57.0, 34.0, 180.0.toRadians)
        var builder0 = TrajectoryBuilder(myPose, 0.0,constraints, accelConstraint)
        builder0
            .strafeTo(Vector2d(57.0, 37.5))
            .splineTo(Vector2d(55.5, 39.0), (90.0).toRadians)
        .lineToConstantHeading(Vector2d(56.5, 28.0))

        list.add(builder0.build())

        return list
    }


    fun buildFoundaton2Trajectory(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()
        val myPose = Pose2d(61.0, -36.0, Math.PI)
        var builder0 = TrajectoryBuilder(myPose, Math.PI, constraints, accelConstraint)
        builder0.splineTo(Vector2d(0.0, -40.0), Math.PI)
            .lineTo(Vector2d(-27.0,-40.0))
            .strafeTo(Vector2d(-27.0,-36.0))
            .splineTo(Vector2d(0.0, -42.0), Math.PI)
            .splineTo(Vector2d(51.0, -45.0), Math.PI/2)
          //  .addTemporalMarker(4.0, MarkerCallback{ println("test marker 0....")})
//            .addMarker (Vector2d(49.0,36.0), { println("test marker spatial....")})

        list.add(builder0.build())

        builder0 = TrajectoryBuilder(Pose2d(53.0, -31.0, Math.PI/2), Math.PI/2,constraints, accelConstraint)
        builder0
            .splineTo( Vector2d(35.0, -58.0), 0.0)
            .splineTo( Vector2d(5.0, -40.0), 0.0)

        //.lineTo( Vector2d(5.0, -40.0))
        list.add(builder0.build())

        return list
    }

    /**
     * This is a demo to showcase different path builder functions
     * Road Runner Coordinates:
     *              (+x)
     *               |
     *               |
     *               | f (foundation)
     *               |
     * (+y) -------(0,0)--- o (Robot) --- (-y)
     *               |
     *               |  s
     *               |  s (stone)
     *               |  s
     *               |
     *              -x
     */
    fun randomPathTest():ArrayList<Trajectory> {

        val list = ArrayList<Trajectory>()
        // using road runner 0.5.0, to make robot run in reverse
        // you need to plus Math.PI to the start and ending heading
        //----------------------------------------------------------------------------
        // run reverse using splineTo, with Linear and Spline Heading interpolator
        var builder = TrajectoryBuilder(
                           Pose2d(0.0, -40.0, Math.PI+Math.PI), Math.PI,constraints, accelConstraint)
        builder.splineToLinearHeading(
                           Pose2d(50.0, -50.0, Math.PI*1.5), Math.PI*1/2)
               .splineToSplineHeading(
                           Pose2d(0.0, -40.0, Math.PI), Math.PI)
        list.add(builder.build())

//        // run forward using splienTo, with default Tangent heading interpolator
//        // and also lineTo with Spline heading interpolator
//        // strafeTo = lineTo with Constant heading interpolator
//        builder = TrajectoryBuilder(Pose2d(0.0, -40.0, Math.PI), Math.PI,,constraints, accelConstraint)
//        builder.splineTo(Pose2d(-35.0, -35.0, Math.PI*3/4))
//               .lineToSplineHeading(Vector2d(-30.0, -55.0), Math.PI*1/2)
//               .strafeTo(Vector2d(0.0, -40.0))
//        list.add(builder.build())
//
//        // run reverse, spline heading with multiple turns
//        var currentPose = Pose2d(0.0, -40.0, Math.PI+ Math.PI/2)
//        builder = TrajectoryBuilder(currentPose,Math.PI/2,,constraints, accelConstraint)
//        builder.splineTo(Pose2d(55.0, -35.0, Math.PI))
//               .lineToSplineHeading(Vector2d(-18.0, -37.0), Math.PI)
//        list.add(builder.build())

        return list
    }

    fun drawOffbounds1() {
        GraphicsUtil.fillShootingPosition(Vector2d(-36.0,-15.0))
        GraphicsUtil.fillShootingPosition(Vector2d(-36.0,-36.0))
        GraphicsUtil.fillShootingPosition(Vector2d(-8.0,-36.0))

        GraphicsUtil.fillRect(Vector2d(12.0, -36.0), 18.0, 18.0, false) // robot against the wall
    }

    fun drawOffbounds2() {
        // draw ring and wobble initial positions
        GraphicsUtil.fillRing(Vector2d(-24.0, -36.0), 2.5)
        GraphicsUtil.fillWobbleInStarting(Vector2d(-48.0, -48.0), 4.0)

        // draw wobble in dropping zone
        GraphicsUtil.fillWobbleInDropZone(Vector2d(61.0, -57.0), 4.0)
        GraphicsUtil.fillWobbleInDropZone(Vector2d(56.0, -65.0), 4.0)

        // draw bounced back rings
        GraphicsUtil.fillRing(Vector2d(30.0, 10.0), 2.5)
        GraphicsUtil.fillRing(Vector2d(48.0, -12.0), 2.5)
    }

}

val Double.toRadians get() = (Math.toRadians(this))

