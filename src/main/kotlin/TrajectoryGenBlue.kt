import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints

// this is skystone test program
object TrajectoryGenBlue {
    private val constraints = DriveConstraints(45.0, 35.0, 0.0, 180.0.toRadians, 180.0.toRadians, 0.0)


    fun createTrajectory(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()

        var startPose = Pose2d(-60.0, 18.0, 0.0.toRadians)
        var builder1 = TrajectoryBuilder(startPose, 0.0,constraints)

        builder1 = TrajectoryBuilder(startPose, 0.0,constraints)
        builder1
            .splineTo(Vector2d(0.0,34.0), 0.0.toRadians)

        // 2.
        list.add(builder1.build())

        builder1 = TrajectoryBuilder(Pose2d(0.0, 34.0, 0.0), 0.0, constraints)
        builder1
            .splineTo(Vector2d(40.0,40.0), 0.0.toRadians)

        return list
    }

    fun drawOffbounds1() {
        GraphicsUtil.fillRect(Vector2d(-12.0, 63.0), 18.0, 18.0, false) // robot against the wall
    }

    fun drawOffbounds2() {
        GraphicsUtil.fillRect(Vector2d(0.0, 24.0), 12.0, 8.0,true) // block against the bridge
    }
}
