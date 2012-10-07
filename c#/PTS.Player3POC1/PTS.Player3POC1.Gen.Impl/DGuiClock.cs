using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Gui
{
    public class DGuiClock : DGuiComponent, IGuiClock
    {
        public String timeFormat;

        public string getTimeFormat()
        {
            return timeFormat;
        }
    }
}
