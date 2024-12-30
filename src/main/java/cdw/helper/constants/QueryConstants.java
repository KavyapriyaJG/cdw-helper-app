package cdw.helper.constants;

/**
 * Constants for MySQL query
 */
public class QueryConstants {
    public static final String HELPERS_NOT_IN_APPOINTMENTS = "SELECT hu.id, hud.first_name, hud.last_name, hd.specialization, hd.hourly_rate, hd.rating from helper_app.user as hu\n" +
            "\tINNER JOIN helper_app.role as hr\n" +
            "    ON hu.role_id = hr.id\n" +
            "    INNER JOIN helper_app.user_details as hud\n" +
            "    ON hud.id = hu.user_details_id\n" +
            "    INNER JOIN helper_app.user_details_helper_details as udhd\n" +
            "    ON udhd.user_details_id = hud.id\n" +
            "    INNER JOIN helper_app.helper_details as hd\n" +
            "    ON udhd.helper_details_id = hd.id\n" +
            "\tWHERE\n" +
            "\t\thu.id NOT IN (\n" +
            "\t\t\tSELECT hu.id FROM helper_app.appointment as ha\n" +
            "\t\t\t\tINNER JOIN helper_app.user as hu\n" +
            "\t\t\t\tON ha.helper_id = hu.id\n" +
            "\t\t\t\tINNER JOIN helper_app.user_details as hud\n" +
            "\t\t\t\tON hu.user_details_id = hud.id\n" +
            "\t\t\t\tINNER JOIN user_details_helper_details as udhd\n" +
            "\t\t\t\tON hud.id = udhd.helper_details_id\n" +
            "\t\t\t\tINNER JOIN helper_app.helper_details as hd\n" +
            "\t\t\t\tON udhd.helper_details_id = hd.id\n" +
            "\t\t\t\tWHERE ha.scheduled_at = :dateTime\n" +
            "\t\t\t)\n" +
            "\t\tAND \n" +
            "        CASE \n" +
            "\t\t\tWHEN hd.specialization != :skill THEN FALSE\n" +
            "\t\t\tELSE hr.name = 'ROLE_HELPER'\n" +
            "\t\tEND;\n";
}
