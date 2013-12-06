package maru.ui.views.timeline;

interface ITimePlayerListener
{
    void saveCurrentTimepoint();
    void selectPreviousTimepoint();
    void selectNextTimepoint();
    void playBackward(int stepSize);
    void playForward(int stepSize);
    void playRealtime(long time);
}
