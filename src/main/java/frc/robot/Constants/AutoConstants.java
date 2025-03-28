package frc.robot.constants;

import java.util.HashMap;

public class AutoConstants {
    public static final double x_goal = -0.4104946267604828;
    public static final double y_goal = 0.11346174776554108;

    public static final double theta_p = 0.02;
    public static final double theta_i = 0;
    public static final double theta_d = 0;

    public static final double x_tolerance_meters = 0;
    public static final double y_tolerance_meters = 0;
    public static final double theta_tolerance_radians = 0;

    public static final double x_p = 0.1;
    public static final double x_i = 0;
    public static final double x_d = 0;

    public static final double y_p = 0.1;
    public static final double y_i = 0;
    public static final double y_d = 0;

    public static final double drive_p = 7;
    public static final double drive_i = 0;
    public static final double drive_d = 2.005;

    public static final double max_velocity = 0;
    public static final double max_acceleration = 0;

    public static HashMap<String, double[]> get_reef_waypoints(String field_side) {
        HashMap<String, double[]> waypoints = new HashMap<>();

        switch (field_side) {
            case "blue_left":
                waypoints.put("7", new double[] { 2.993, 4.161, 0 });
                waypoints.put("12", new double[] { 3.617, 2.826, 60.255 });
                waypoints.put("11", new double[] { 5.060, 2.650, 119.982 });
                waypoints.put("10", new double[] { 5.977, 3.859, 180.000 });
                waypoints.put("9", new double[] { 5.372, 5.215, -119.427 });
                waypoints.put("8", new double[] { 3.920, 5.419, -60.124 });

                break;

            case "blue_right":
                waypoints.put("7", new double[] { 2.993, 3.859, 0 });
                waypoints.put("12", new double[] { 3.900, 2.660, 60.255 });
                waypoints.put("11", new double[] { 5.362, 2.816, 119.982 });
                waypoints.put("10", new double[] { 5.977, 4.161, 180.000 });
                waypoints.put("9", new double[] { 5.080, 5.410, -119.427 });
                waypoints.put("8", new double[] { 3.607, 5.244, -60.124 });

                break;

            case "red_left":
                waypoints.put("7", new double[] { 14.537, 4.161, 180.000 });
                waypoints.put("12", new double[] { 13.913, 2.826, 119.745 });
                waypoints.put("11", new double[] { 12.470, 2.650, 60.018 });
                waypoints.put("10", new double[] { 11.553, 3.859, 0.000 });
                waypoints.put("9", new double[] { 12.158, 5.215, -60.573 });
                waypoints.put("8", new double[] { 13.610, 5.419, -119.876 });

                break;

            case "red_right":
                waypoints.put("7", new double[] { 14.537, 3.859, 180.000 });
                waypoints.put("12", new double[] { 13.630, 2.660, 119.745 });
                waypoints.put("11", new double[] { 12.168, 2.816, 60.018 });
                waypoints.put("10", new double[] { 11.553, 4.161, 0.000 });
                waypoints.put("9", new double[] { 12.450, 5.410, -60.573 });
                waypoints.put("8", new double[] { 13.923, 5.244, -119.876 });
            
                break;

            default:
                break;
        }

        return waypoints;
    }

    public static HashMap<String, double[]> get_intake_waypoints(String field_side) {
        HashMap<String, double[]> waypoints = new HashMap<String, double[]>();

        switch (field_side) {
            case "blue":
                waypoints.put("right", new double[] { 1.199, 1.022, 54.926 });
                waypoints.put("left", new double[] { 1.199, 6.999, -53.807 });

            case "red":
                waypoints.put("right", new double[] { 16.331, 1.022, 125.074 });
                waypoints.put("left", new double[] { 16.331, 6.999, -126.193 });
        }

        return waypoints;
    }
}
