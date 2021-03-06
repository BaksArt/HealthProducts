package com.example.healthproducts.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthproducts.R;
import com.example.healthproducts.server.rest.HealthProductsApiVolley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ScannerFragment extends Fragment {
    private Document doc;
    private Thread parseThread;
    private Runnable parseRunnable;
    private SurfaceView surfaceView;
    private CameraSource cameraSrc;
    private static final int CAMERA_PERMISSION = 201;
    private String barcodeData;
    private TextView barcodeText;
    private BarcodeDetector barcodeDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        surfaceView = view.findViewById(R.id.surface_view);
        barcodeText = view.findViewById(R.id.barcode_text);


        HealthProductsApiVolley healthProductsApiVolley = new HealthProductsApiVolley(getContext());
        healthProductsApiVolley.fillProduct(); //обновляем базу данных перед сканированием
        healthProductsApiVolley.fillCategory();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    public void onStart() {
        super.onStart();
        initialiseDetectorsAndSources();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSrc.release();
    }

    private void initialiseDetectorsAndSources() { //метод для включения камеры и детектеров


        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSrc = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSrc.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSrc.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                            }

                            Toast.makeText(getContext(), "Код отсканирован", Toast.LENGTH_SHORT).show();
                            cameraSrc.release();


                            SearchProductFragment searchProductFragment = new SearchProductFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("code", barcodeData); //передаем код продукта


                            searchProductFragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container, searchProductFragment)
                                    .commit();

                        }
                    });


                }
            }
        });
    }
}