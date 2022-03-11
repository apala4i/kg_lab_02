package com.example.demo1;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.*;

import java.net.URL;
import java.util.Stack;
import java.util.ResourceBundle;

public class HelloController{


    @FXML
    private TextField moveX;

    @FXML
    private TextField scaleValueX;

    @FXML
    private TextField scaleValueY;

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

    Double angle = 0.;

    Point2D scale = new Point2D(1, 1);

    TranslationLogger aTranslationLogger = new TranslationLogger();


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
        catch (Exception e)
        {
            showAlertWithMSG("что-то пошлшо не так");
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
        g.fillOval(x - pointHeight / 2 , y - pointHeight / 2, pointWidth, pointHeight);
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
//        gc.lineTo(270, 150);
//        gc.lineTo(300, 100);
//        gc.moveTo(270, 150);
//        gc.lineTo(220, 200);
//        gc.moveTo(290, 100);
//        gc.lineTo(290, 200);
//        gc.lineTo(390, 100);
//        gc.lineTo(390, 200);

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


            double doubleMoveX = Double.parseDouble(moveX.getText()) / scale.getX();
            double doubleMoveY = Double.parseDouble(moveY.getText()) / scale.getY();
            double newX = doubleMoveX * Math.cos(Math.toRadians(-angle)) - doubleMoveY * Math.sin(Math.toRadians(-angle));
            double newY = doubleMoveX * Math.sin(Math.toRadians(-angle)) + doubleMoveY * Math.cos(Math.toRadians(-angle));

            Translate moveMatrix;

            if (angle != 0)
            {
                moveMatrix = Affine.translate(newX, newY);
            }
            else
            {
                moveMatrix = Affine.translate(doubleMoveX, doubleMoveY);
            }

            var transforms = aCanvas.getTransforms();
            transforms.add(moveMatrix);
            centerShift = centerShift.add(-doubleMoveX, -doubleMoveY);

            aTranslationLogger.transformLogger(translationLogger, new Point2D(doubleMoveX, doubleMoveY));
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
            if (scaleValueX.getText().length() == 0 ||
                scaleValueY.getText().length() == 0 ||
                centerX.getText().length() == 0 ||
                centerY.getText().length() == 0)
            {
                throw new NullPointerException();
            }
            var center = getCenter();
            double doubleScaleValueX = Double.parseDouble(scaleValueX.getText());
            double doubleScaleValueY = Double.parseDouble(scaleValueY.getText());
            var scaleMatrix = Affine.scale(doubleScaleValueX, doubleScaleValueY, center.getX(), center.getY());
            var transforms = aCanvas.getTransforms();
            transforms.add(scaleMatrix);

            aTranslationLogger.scaleLogger(translationLogger, new Point2D(doubleScaleValueX, doubleScaleValueY));
            scale = new Point2D(scale.getX()*doubleScaleValueX, scale.getY()*doubleScaleValueY);
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

            aTranslationLogger.rotateLogger(translationLogger, doubleRotateValue);
            angle += doubleRotateValue;

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

            TransformInfo lastTransformInfo = aTranslationLogger.removeLastTranslation(translationLogger);
            switch (lastTransformInfo.aTransformType)
            {
                case Move:
                    centerShift = centerShift.add(transforms.get(transforms.size() - 1).getTx(),
                            transforms.get(transforms.size() - 1).getTy());
                    break;
                case Scale:
                    this.scale = new Point2D(scale.getX() / lastTransformInfo.transformValue.getX(),
                                         scale.getY() / lastTransformInfo.transformValue.getY());
                    break;
                case Rotate:
                    this.angle -= lastTransformInfo.transformValue.getX();
                    break;
                default:
                    ;
            }
            transforms.remove(aCanvas.getTransforms().size() - 1);
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