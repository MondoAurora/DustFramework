using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;
using Dust.Tools;

namespace Dust.Gui
{
    public interface IGuiFlip : IGuiComponent
    {
        IToolsListSelector getContent();
    }
}
