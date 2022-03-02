package com.example.demo1;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.transform.*;

import java.net.URL;
import java.util.Stack;
import java.util.ResourceBundle;

public class HelloController{


    @FXML
    private TextField moveX;

    @FXML
    private TextField scaleValue;

    @FXML
    private TextField centerX;

    @FXML
    private TextField centerY;


    @FXML
    private TextField moveY;


    @FXML
    private TextField rotateValue;

    @FXML
    private Canvas aCanvasForCenter;

    @FXML
    private TextArea translationLogger;

    Point2D centerShift = new Point2D(0, 0);


    @FXML
    private void initialize()
    {
        addFigureButton();
        TranslationLogger.launchTranslation(translationLogger);
        drawCenter();
    }

    @FXML
    private void changedCenter()
    {
        try
        {
            double currentCenterX = Double.parseDouble(centerX.getText());
            double currentCenterY = Double.parseDouble(centerY.getText());
            var gc = aCanvasForCenter.getGraphicsContext2D();
            gc.clearRect(0, 0, aCanvasForCenter.getWidth(), aCanvasForCenter.getHeight());
            drawPointWithCoord(currentCenterX, currentCenterY);
        }
        catch (NullPointerException e)
        {
            showAlertWithMSG("Некорректно задан центр");
        }
    }

    private void drawCenter()
    {
        try
        {
            double currentCenterX = Double.parseDouble(centerX.getText());
            double currentCenterY = Double.parseDouble(centerY.getText());
            drawPointWithCoord(currentCenterX, currentCenterY);
        }
        catch (NullPointerException e)
        {
            showAlertWithMSG("Некорректно задан центр");
        }
    }

    private void drawPointWithCoord(double x, double y)
    {
        final double pointHeight = 5;
        final double pointWidth = 5;
        var g = aCanvasForCenter.getGraphicsContext2D();
        g.fillOval(x - pointWidth*2, y - pointHeight*2, pointWidth, pointHeight);
        g.fillText(Double.valueOf(x).toString() + " " + Double.valueOf(y).toString(), x - 40, y - pointWidth*3);
        g.stroke();
    }


    private void drawPointWithCoord(double x, double y, Canvas aCanvas)
    {
        final double pointHeight = 5;
        final double pointWidth = 5;
        var g = aCanvas.getGraphicsContext2D();
        g.fillOval(x - pointWidth/2, y - pointHeight/2, pointWidth, pointHeight);
        g.fillText(Double.valueOf(x).toString() + " " + Double.valueOf(y).toString(), x - 40, y - pointWidth*3);
        g.stroke();
    }

    public void addFigureButton()
    {
        var gc = aCanvas.getGraphicsContext2D();
        gc.beginPath();
        gc.moveTo(100, 100);
        gc.lineTo(200, 200);
        gc.moveTo(100, 200);
        gc.lineTo(200, 100);
        gc.moveTo(220, 100);
        gc.lineTo(270, 150);
        gc.lineTo(300, 100);
        gc.moveTo(270, 150);
        gc.lineTo(220, 200);
        gc.moveTo(290, 100);
        gc.lineTo(290, 200);
        gc.lineTo(390, 100);
        gc.lineTo(390, 200);

        gc.stroke();
    }


    @FXML
    Canvas aCanvas;


    @FXML
    private void moveFigures()
    {
        try
        {
            if (moveX.getText().length() == 0 ||
                moveY.getText().length() == 0 ||
                centerY.getText().length() == 0)
            {
                throw new NullPointerException();
            }

            double doubleMoveX = Double.parseDouble(moveX.getText());
            double doubleMoveY = Double.parseDouble(moveY.getText());
            var moveMatrix = Affine.translate(doubleMoveX, doubleMoveY);
            var transforms = aCanvas.getTransforms();
            transforms.add(moveMatrix);

            centerShift = centerShift.add(-doubleMoveX, -doubleMoveY);

            TranslationLogger.transformLogger(translationLogger, new Point2D(doubleMoveX, doubleMoveY));
        }
        catch (NumberFormatException e)
        {
            showAlertWithMSG("Некорректный ввод числа, попробуйте еще раз!");
        }
        catch (NullPointerException e)
        {
            showAlertWithMSG("Вы не ввели одно из значений, попробуйте еще раз!");
        }

    }

    @FXML
    private void scaleFigures()
    {
        try
        {
            if (scaleValue.getText().length() == 0 ||
                centerX.getText().length() == 0 ||
                centerY.getText().length() == 0)
            {
                throw new NullPointerException();
            }
            var center = getCenter();
            double doubleScaleValue = Double.parseDouble(scaleValue.getText());
            var scaleMatrix = Affine.scale(doubleScaleValue, doubleScaleValue, center.getX(), center.getY());
            var transforms = aCanvas.getTransforms();
            transforms.add(scaleMatrix);

            TranslationLogger.scaleLogger(translationLogger, new Point2D(doubleScaleValue, doubleScaleValue));
        }
        catch (NumberFormatException e)
        {
            showAlertWithMSG("Некорректный ввод, попробуйте еще раз!");
        }
        catch (NullPointerException e)
        {
            showAlertWithMSG("Вы не ввели одно из значений, попробуйте еще раз!");
        }
    }

    @FXML
    private void rotateFigures()
    {
        try
        {
            if (rotateValue.getText().length() == 0 ||
                centerX.getText().length() == 0 ||
                centerY.getText().length() == 0)
            {
                throw new NullPointerException();
            }

            double doubleRotateValue = Double.parseDouble(rotateValue.getText());
            var center = getCenter();

            var rotateMatrix = Affine.rotate(doubleRotateValue, center.getX(),
                                                    center.getY());
            var transforms = aCanvas.getTransforms();
            transforms.add(rotateMatrix);

            TranslationLogger.rotateLogger(translationLogger, doubleRotateValue);

        }
        catch (NumberFormatException e)
        {
            showAlertWithMSG("Некорректный ввод числа, попробуйте еще раз!");
        }
        catch (NullPointerException e)
        {
            showAlertWithMSG("Вы не ввели одно из значений, попробуйте еще раз!");
        }
    }

    @FXML
    private void goBack()
    {
        var curTransforms = aCanvas.getTransforms();
        if (curTransforms.size() == 0)
        {
            showAlertWithMSG("Вы вернулись в исходное состояние, невозможно сделать шаг назад");
        }
        else
        {
            var transforms = aCanvas.getTransforms();


            if (isTransformMatrix(transforms.get(transforms.size() - 1)))
            {
                centerShift = centerShift.add(transforms.get(transforms.size() - 1).getTx(),
                        transforms.get(transforms.size() - 1).getTy());
            }

            transforms.remove(aCanvas.getTransforms().size() - 1);
            TranslationLogger.removeLastTranslation(translationLogger);
        }
    }

    private void showAlertWithMSG(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.setTitle("Предупреждение");
        alert.showAndWait();
    }

    private Point2D getCenter()
    {
        var centerPoint = new Point2D(Double.parseDouble(centerX.getText()),
                                Double.parseDouble(centerY.getText()));

        centerPoint = centerPoint.add(centerShift);

        return centerPoint;
    }

    private boolean isTransformMatrix(Transform aTransform)
    {
        return (Math.abs(aTransform.getMxx() + aTransform.getMxy() + aTransform.getMxz() +
                aTransform.getMyx() + aTransform.getMyy() + aTransform.getMyz() - 2) <= 0.1E-4 &&
                (aTransform.getTx() != 0 || aTransform.getTy() != 0));
    }
}