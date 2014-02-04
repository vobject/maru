package maru.ui.views.timeline;

import org.orekit.time.AbsoluteDate;

interface ITimeChangedListener
{
    void timeChanged(TimeLabelType type, AbsoluteDate date);
}
