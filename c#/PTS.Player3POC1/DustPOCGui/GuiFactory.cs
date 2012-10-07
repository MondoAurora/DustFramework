using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Dust.Core;

namespace Dust.Gui
{
    public class GuiFactory : DustFactory
    {
        public DustComponentBase createComponentForData(DustCompData data)
        {
            if (data is IGuiTable)
            {
                return new GuiTable();
            }
            else if (data is IGuiText)
            {
                return new GuiText();
            }
            else if (data is IGuiClock)
            {
                return new GuiClock();
            }
            else if (data is IGuiXaml)
            {
                return new GuiXaml();
            }
            else if (data is IGuiFlip)
            {
                return new GuiFlip();
            } 

            return null;
        }
    }
}
