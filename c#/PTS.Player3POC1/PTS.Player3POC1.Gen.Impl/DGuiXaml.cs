using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Gui
{
    public class DGuiXaml : DGuiComponent, IGuiXaml
    {
        public String xamlPath;

        public string getXamlPath()
        {
            return xamlPath;
        }
    }
}
