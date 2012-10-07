using Dust.Core;
using Dust.Tools;

namespace Dust.Media
{
    public class MediaItemRefSelectionAction : ToolsAction<DMediaItemRefSelectionAction>
    {
        protected override void execute(DustMessage param, DustData source)
        {
            DMediaItemRefSelectionAction d = getData();
            IMediaItem selMi = (IMediaItem) ((MIToolsActionExecute)param).getParam();

            send(new MMediaGuiComponentSetMedia() { item = selMi.getReferredItem() }, d.getTarget());
        }
    }
}
