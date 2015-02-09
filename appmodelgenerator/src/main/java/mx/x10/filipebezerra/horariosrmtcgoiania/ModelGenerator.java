package mx.x10.filipebezerra.horariosrmtcgoiania;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the data model project.
 *
 * This class must be performed as a Java application (not Android).
 *
 * @author Filipe Bezerra
 * @version 2.0
 * @since 25/11/2014
 */
public class ModelGenerator {

    private static final int DATABASE_VERSION = 1;
    private static final String MODEL_PACKAGE = "mx.x10.filipebezerra.horariosrmtcgoiania.model";
    private static final String DAO_PACKAGE = MODEL_PACKAGE + ".dao";
    private static final String FAVORITE_BUS_STOP_ENTITY = "FavoriteBusStop";
    private static final String JAVA_OUTPUT_DIR = "../app/src/main/java-gen";
    private static final String TEST_OUTPUT_DIR = "../app/src/main/test-gen";

    private static Schema mSchema;

    public static void main(String[] args) {
        prepareSchema();
        prepareEntities();

        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(mSchema, JAVA_OUTPUT_DIR, TEST_OUTPUT_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void prepareEntities() {
        if (mSchema == null) {
            throw new IllegalArgumentException("Schema não pode ser nulo.");
        }

        Entity favoriteBusStop = mSchema.addEntity(FAVORITE_BUS_STOP_ENTITY);
        favoriteBusStop.addIdProperty()
                .autoincrement()
                .unique();
        favoriteBusStop.addIntProperty("stopCode")
                .notNull()
                .unique()
                .index();
        favoriteBusStop.addStringProperty("address")
                .notNull();
        favoriteBusStop.addStringProperty("stopReference");
    }

    private static void prepareSchema() {
        mSchema = new Schema(DATABASE_VERSION, MODEL_PACKAGE);
        mSchema.setDefaultJavaPackageDao(DAO_PACKAGE);
    }

}
