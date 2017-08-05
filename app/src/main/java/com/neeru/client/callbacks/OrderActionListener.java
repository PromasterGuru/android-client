package com.neeru.client.callbacks;

/**
 * Created by brajendra on 05/08/17.
 */

public interface OrderActionListener {

    void onTimeSlotSelection(String time);
    void onCanSelected(String time);
}
