package dashboard.core.utils;


import org.gridgain.grid.Grid;
import org.gridgain.grid.GridGain;

public class GridUtils {

    public static Grid getGrid() {
        return GridGain.grid(GridConstants.GRID_NAME);
    }
}
