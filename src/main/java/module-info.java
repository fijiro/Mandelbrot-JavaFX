module iiro.toiv.mandelbrotjavafx {
    requires javafx.controls;

    opens iiro.toiv.mandelbrotjavafx;
    exports iiro.toiv.mandelbrotjavafx;
    exports iiro.toiv.mandelbrotjavafx.Graphics;
    opens iiro.toiv.mandelbrotjavafx.Graphics;
    exports iiro.toiv.mandelbrotjavafx.Input;
    opens iiro.toiv.mandelbrotjavafx.Input;
    exports iiro.toiv.mandelbrotjavafx.Positions;
    opens iiro.toiv.mandelbrotjavafx.Positions;
}