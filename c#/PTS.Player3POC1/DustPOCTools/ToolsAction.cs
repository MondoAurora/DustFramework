using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Dust.Core;

namespace Dust.Tools
{
    public abstract class ToolsAction<DataType> : DustComponent<DataType> where DataType : DustCompData
    {
        public override ProcessReturn process(DustMessage message, DustCompData source)
        {
            switch ((EToolsActionMessage)message.getMessageId())
            {
                case EToolsActionMessage.Execute:
                    execute(message, source);
                    break;
            }
            return ProcessReturn.Acknowledged;
        }

        protected abstract void execute(DustMessage msgEx, DustData source);
    }

    public class ToolsActionMessage : ToolsAction<DToolsActionMessage>
    {
        protected override void execute(DustMessage param, DustData source)
        {
            DToolsActionMessage d = getData();

            send(d.getMessage(), d.getTarget());
        }
    }

    /*This is not good perhaps, sending the messages through the message queue...*/
    public class ToolsActionList : ToolsAction <DToolsActionList>
    {
        protected override void execute(DustMessage msgEx, DustData source)
        {
            foreach (DustData ai in getData().getMembers().getItems())
            {
                send(msgEx, (DustCompData) ai);
            }
        }
    }
}
