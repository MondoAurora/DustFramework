using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Gui
{
    public class DGuiText : DGuiComponent, IGuiText
    {
        public String text;
        public DustData actData;

        public string getText()
        {
            return text;
        }

        public DustData getActivationData()
        {
            return actData;
        }
    }
}
