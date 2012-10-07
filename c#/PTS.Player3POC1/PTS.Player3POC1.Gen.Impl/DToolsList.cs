using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Tools
{
    public class DToolsList : DustCompData, IToolsList
    {
        public List<DustData> items;

        public DToolsList()
        {
            items = new List<DustData>();
        }

        public DToolsList(DustData item) : this()
        {
            items.Add(item);
        }

        public DToolsList(IEnumerable<DustData> content)
        {
            items = new List<DustData>(content);

            if (null != id)
            {
                int i = 0;
                foreach (DustData d in items)
                {
                    if (null == d.id)
                    {
                        d.id = new DustIdentifier(i.ToString(), this.id);
                    }
                    else
                    {
                        d.id.setParent(this.id);
                    }
                    ++i;
                }
            }
        }

        public IEnumerable<DustData> getItems() { return items; }
        public int getCount() { return items.Count; }
        public int indexOf(DustData ob) { return items.IndexOf(ob); }
        public DustData get(int idx) { return items[idx]; }
    }

    public class MToolsListClear : DustMessage
    {
        public override Enum getMessageId() { return EToolsListMessage.Clear; }
    }

    public abstract class MToolsListBase : DustMessage, MIToolsListBase
    {
        public List<DustData> items;

        protected MToolsListBase(IEnumerable<DustData> sel = null)
        {
            items = (null == sel) ? new List<DustData>() : new List<DustData>(sel);
        }

        protected MToolsListBase(DustData sel)
        {
            items = new List<DustData>();
            items.Add(sel);
        }

        public IEnumerable<DustData> getItems()
        {
            return items;
        }

        public DustData getItem()
        {
            return (0 == items.Count) ? null : items.First();
        }
    }

    public class MToolsListAdd : MToolsListBase
    {
        public MToolsListAdd() : base() { }
        public MToolsListAdd(IEnumerable<DustData> sel) : base(sel) { }
        public MToolsListAdd(DustData sel) : base(sel) { }
        public override Enum getMessageId() { return EToolsListMessage.Add; }
    }

    public class MToolsListRemove : MToolsListBase
    {
        public MToolsListRemove() : base() { }
        public MToolsListRemove(IEnumerable<DustData> sel) : base(sel) { }
        public MToolsListRemove(DustData sel) : base(sel) { }
        public override Enum getMessageId() { return EToolsListMessage.Remove; }
    }
}
