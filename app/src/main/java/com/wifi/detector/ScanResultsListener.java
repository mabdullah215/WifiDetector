package com.wifi.detector;

import java.util.List;

public interface ScanResultsListener
{
    void onScanResultsReceived(List results);
}