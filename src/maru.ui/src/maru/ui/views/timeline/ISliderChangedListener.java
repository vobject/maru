package maru.ui.views.timeline;

import org.orekit.time.AbsoluteDate;

interface ISliderChangedListener
{
    void currentTimeChanged(AbsoluteDate date);
}
