package maru.ui.views.timeline;

import org.orekit.time.AbsoluteDate;

interface ITimePlayerListener
{
    void saveCurrentTimepoint();
    void selectPreviousTimepoint();
    void selectNextTimepoint();
    void playBackward(int stepSize);
    void playForward(int stepSize);
    void playRealtime(AbsoluteDate date);
}
