using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Dust.Core;

namespace Dust.Tools
{
    public class ToolsListSelector : DustComponent <DToolsListSelector>
    {
        public override void init() {
            listen(new DustMessageFilter((DustCompData)getData().getList()));
        }

        public override ProcessReturn process(DustMessage message, DustCompData source)
        {
            if (source == this.getDataDef())
            {
                return ProcessReturn.Unhandled;
            }
            bool broadcast = false;
            DToolsListSelector data = getData();
            IToolsList content = data.getList();
            int contentCount = content.getCount();

            if (message.getMessageId() is EToolsListSelectorMessage)
            {
                switch ((EToolsListSelectorMessage)message.getMessageId())
                {
                    case EToolsListSelectorMessage.SelectNext:
                        if (0 < contentCount)
                        {
                            DustData sel = (0 < data.selected.Count) ? data.selected.First() : null;
                            int nextIdx = (null == sel) ? 0 : content.indexOf(sel) + 1;
                            if (contentCount <= nextIdx)
                            {
                                nextIdx = nextIdx % contentCount;
                            }
                            broadcast = selectSingle(content.get(nextIdx));
                        }
                        break;
                    case EToolsListSelectorMessage.Select:
                        broadcast = selectSingle(((MIToolsListBase)message).getItem());
                        break;

                }
            }
            else if (message.getMessageId() is EToolsListMessage)
            {
                // listened child changed, update the selected collection
            }

            return broadcast ? ProcessReturn.Updated : ProcessReturn.Acknowledged;
        }

        bool selectSingle(DustData ob)
        {
            DToolsListSelector data = getData();
            if ((1 == data.selected.Count) && (ob == data.selected.First()))
            {
                return false;
            }

            data.selected.Clear();
            data.selected.Add(ob);

            return true;
        }
    }
}
