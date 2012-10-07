using System;
using Dust.Core;
using System.Windows.Forms;
using Dust.Tools;

namespace Dust.Gui
{
    public class DGuiFlip : DGuiComponent, IGuiFlip
    {
        public IToolsListSelector content;

        public IToolsListSelector getContent()
        {
            return content;
        }
    }
}
