using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;
using Dust.Tools;

namespace Dust.Gui
{
    public enum EGuiComponentMessage { Activated, Interacted, Displayed, Hidden } 

    public interface IGuiComponent
    {
        Control getGuiCtrl();
        IToolsList getEventHandlers();
    }

    public interface MIGuiComponentActivated
    {
        DustData getActivatedObject();
    }
}
