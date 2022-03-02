package com.example.demo1;

import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;

public class TranslationLogger
{
    public static void transformLogger(TextArea aTextArea, Point2D move)
    {
        String addText = String.format("Фигура перемещена на (%.2f, %.2f)\n",
                                        move.getX(), move.getY());
        aTextArea.setText(aTextArea.getText() + addText);
    }

    public static void rotateLogger(TextArea aTextArea, double angle)
    {
        aTextArea.setText(aTextArea.getText() + String.format("Фигура повернута на %.2f градуоов\n", angle));
    }

    public static void scaleLogger(TextArea aTextArea, Point2D scale)
    {
        aTextArea.setText(aTextArea.getText() + String.format("Фигура растянута с коэффицентами (%.2f, %.2f)\n",
                                                              scale.getX(), scale.getY()));
    }

    public static void removeLastTranslation(TextArea aTextArea)
    {
        String currentText = aTextArea.getText();
        if (currentText.length() != 0)
        {
            if (stringCount(currentText, "\n") != 1)
            {
                var tmp = (currentText.substring(0, currentText.length() - 1));
                aTextArea.setText(tmp.substring(0, tmp.lastIndexOf("\n")) + "\n");
            }
            else
            {
                aTextArea.setText("");
            }
        }
    }

    public  static void launchTranslation(TextArea aTextArea)
    {
        aTextArea.setText("Фигура нарисована\n");
    }

    public static int stringCount(String aString, String key)
    {
        return aString.length() - aString.replace("\n", "").length();
    }
}
