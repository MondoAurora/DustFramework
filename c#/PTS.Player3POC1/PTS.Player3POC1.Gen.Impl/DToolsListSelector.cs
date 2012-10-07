using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Tools
{
    public class DToolsListSelector : DustCompData, IToolsListSelector
    {
        public IToolsList list;
        public List<DustData> selected;

        public DToolsListSelector()
        {
            selected = new List<DustData>();
        }

        public DToolsListSelector(IToolsList list, int initSel) : this()
        {
            this.list = list;
            selected.Add(list.get(initSel));               
        }

        public DToolsListSelector(IEnumerable<DustData> sel)
        {
            selected = new List<DustData>(sel);
        }

        public DToolsListSelector(DustData sel) : this()
        {
            selected.Add(sel);
        }

        public IToolsList getList()
        {
            return list;
        }

        public IEnumerable<DustData> getSelected()
        {
            return selected;
        }


        public DustData getSelectedSingle()
        {
            return (0 == selected.Count) ? null : selected.First();
        }

        public int getSelectedCount()
        {
            return selected.Count;
        }


        public bool isSelected(DustData data)
        {
            return selected.Contains(data);
        }
    }

    public class MToolsListSelectorSelect : MToolsListBase
    {
        public MToolsListSelectorSelect() : base() { }
        public MToolsListSelectorSelect(IEnumerable<DustData> sel) : base(sel) { }
        public MToolsListSelectorSelect(DustData sel) : base(sel) { }
        public override Enum getMessageId() { return EToolsListSelectorMessage.Select; }
    }

    public class MToolsListSelectorUnselect : MToolsListBase
    {
        public MToolsListSelectorUnselect() : base() { }
        public MToolsListSelectorUnselect(IEnumerable<DustData> sel) : base(sel) {}
        public MToolsListSelectorUnselect(DustData sel) : base(sel) { }
        public override Enum getMessageId() { return EToolsListSelectorMessage.Unselect; }
    }

    public class MToolsListSelectorClearSelection : DustMessage
    {
        public override Enum getMessageId() { return EToolsListSelectorMessage.ClearSelection; }
    }

    public class MToolsListSelectorSelectNext : DustMessage
    {
        public override Enum getMessageId() { return EToolsListSelectorMessage.SelectNext; }
    }
}
