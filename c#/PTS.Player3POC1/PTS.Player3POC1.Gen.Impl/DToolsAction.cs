using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Tools
{
    public class DToolsAction : DustCompData, IToolsAction
    {
    }

    public class DToolsActionMessage : DToolsAction, IToolsActionMessage
    {
        public DustCompData target;
        public DustMessage message;

        public DustCompData getTarget()
        {
            return target;
        }

        public DustMessage getMessage()
        {
            return message;
        }
    }

    public class DToolsActionList : DToolsAction, IToolsActionList
    {
        public IToolsList members;

        public IToolsList getMembers()
        {
            return members;
        }
    }

    public class MToolsActionExecute : DustMessage, MIToolsActionExecute
    {
        public DustData param;

        public DustData getParam()
        {
            return param;
        }

        public override Enum getMessageId()
        {
            return EToolsActionMessage.Execute;
        }
    }
}
