using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Dust.Core;

namespace Dust.Tools
{
    public class ToolsList : DustComponent <DToolsList>
    {
        public override ProcessReturn process(DustMessage message, DustCompData source)
        {
            DToolsList data = getData();

            int count = data.items.Count;

            switch ((EToolsListMessage)message.getMessageId())
            {
                case EToolsListMessage.Clear:
                    if (0 < count)
                    {
                        data.items.Clear();
                    }
                    break;
                case EToolsListMessage.Add:
                    {
                        foreach (DustData ob in ((MIToolsListBase)message).getItems())
                        {
                            if (!data.items.Contains(ob))
                            {
                                data.items.Add(ob);
                            }
                        }
                    }
                    break;
                case EToolsListMessage.Remove:
                    if (0 < count)
                    {
                        foreach (DustData ob in ((MIToolsListBase)message).getItems())
                        {
                            data.items.Remove(ob);
                        }
                    }
                    break;

            }

            return (count == data.items.Count) ? ProcessReturn.Acknowledged : ProcessReturn.Updated;
        }
    }
}
