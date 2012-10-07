using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Tools
{
    public enum EToolsActionMessage { Execute };

    public interface IToolsAction
    {
    }

    public interface MIToolsActionExecute
    {
        DustData getParam();
    }

    public interface IToolsActionList : IToolsAction
    {
        IToolsList getMembers();
    }

    public interface IToolsActionMessage : IToolsAction
    {
        DustCompData getTarget();
        DustMessage getMessage();
    }
}
