package dashboard.utils;


import org.gridgain.grid.Grid;
import org.gridgain.grid.GridFactory;

public class GridUtils {

    public static final String GRID_NAME = "twitter-grid";

    public static Grid getGrid() {
        return GridFactory.grid(GRID_NAME);
    }
}
