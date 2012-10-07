using Dust.Core;

namespace Dust.Tools
{
    public class ToolsFactory : DustFactory
    {
        public DustComponentBase createComponentForData(DustCompData data)
        {
            if (data is IToolsList)
            {
                return new ToolsList();
            }
            else if (data is IToolsListSelector)
            {
                return new ToolsListSelector();
            }
            else if (data is IToolsActionMessage)
            {
                return new ToolsActionMessage();
            }
            else if (data is IToolsActionList)
            {
                return new ToolsActionList();
            }
            else if (data is IToolsScheduler)
            {
                return new ToolsScheduler();
            }
            else if (data is IToolsSchedulerTask)
            {
                return new ToolsSchedulerTask();
            } 

            return null;
        }
    }
}
