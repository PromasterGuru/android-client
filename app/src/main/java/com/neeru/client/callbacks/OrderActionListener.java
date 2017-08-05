package com.neeru.client.callbacks;

/**
 * Created by brajendra on 05/08/17.
 */

public interface OrderActionListener {

    void onTimeSlotSelection(int time);

    void onCanSelected(int quantity);

    void onSlotAvailable(boolean isSlotAvilable);
}
