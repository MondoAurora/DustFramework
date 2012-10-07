using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Tools
{
    public enum EToolsListSelectorMessage { SelectNext, Select, Unselect, ClearSelection };

    public interface IToolsListSelector
    {
        IToolsList getList();
        IEnumerable<DustData> getSelected();
        DustData getSelectedSingle();
        bool isSelected(DustData data);
        int getSelectedCount();
    }
}
