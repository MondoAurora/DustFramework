using System;
using System.IO;
using System.Windows;
using System.Windows.Forms;
using Dust.Core;
using Dust.Gui;
using Dust.Media;
using Dust.Tools;
using JsonExSerializer;

namespace PTS.Player3POC1.Demo2
{
    public class Demo2Player : DustComponent<PTSTerminal>
    {
        public GuiText loader1 = new GuiText();
        public MediaGuiHtml loader2 = new MediaGuiHtml();

        public Demo2Player()
        {
            //            addKnownNamespacePrefix("PTS");
        }

        PTSTerminal build()
        {
            DMediaItem miWait = new DMediaItem { id = new DustIdentifier("Wait"), type = EMediaType.HTML, path = "http://msdn.microsoft.com/en-us/library/syehfawa.aspx" };
            DMediaItem miDateTime = new DMediaItem { id = new DustIdentifier("DateTime"), type = EMediaType.HTML, path = "http://msdn.microsoft.com/en-us/library/aa326681(v=vs.71).aspx" };

            DMediaGuiHtml adHtmlPanel = new DMediaGuiHtml() { item = miWait };

            DToolsActionMessage actFlipInteractive = new DToolsActionMessage() { id = new DustIdentifier(EGuiComponentMessage.Activated.ToString()) };
            DToolsActionMessage actFlipDefault = new DToolsActionMessage() { id = new DustIdentifier(EGuiComponentMessage.Activated.ToString()) };
            DToolsActionMessage actFlipLocal = new DToolsActionMessage() { id = new DustIdentifier(EGuiComponentMessage.Activated.ToString()) };

            DustCompData actThumbActivator = new DToolsActionList()
            {
                id = new DustIdentifier(EGuiComponentMessage.Activated.ToString()),
                members = new DToolsList(new DustData[]{
                    actFlipInteractive,
                    new DMediaItemRefSelectionAction() { target = adHtmlPanel }
                }),
            };

            DToolsList listAdContent = new DToolsList(new DustData[]{
                    miWait,
                    miDateTime,
                }) { id = new DustIdentifier("AdContent") };


            DGuiText rollingText1 = new DGuiText { text = "RollingText1" };
            DGuiText rollingText2 = new DGuiText { text = "RollingText2" };

            DGuiText cmdLocalInfo = new DGuiText
            {
                text = "Local Info",
                actData = miWait,
                eventHandlers = new DToolsList(actFlipLocal),
            };

            DGuiText cmdExit = new DGuiText
            {
                text = "Exit",
                actData = miWait,
                eventHandlers = new DToolsList(actFlipDefault),
            };

            DGuiText localMenu = new DGuiText { text = "Local Menu" };


            DToolsList listRollerContent = new DToolsList(new DustData[] { 
                new DMediaItem(EMediaType.Image, "resources\\1.jpg", miWait), 
                new DMediaItem(EMediaType.Image, "resources\\2.jpg", miWait), 
                new DMediaItem(EMediaType.Image, "resources\\3.jpg", miDateTime),
            });

            DToolsListSelector mediaListSel = new DToolsListSelector(listRollerContent, 0);

            DToolsSchedulerTask taskRoller = new DToolsSchedulerTask()
            {
                type = EToolsSchedulerTaskType.Repeat,
                waitMsec = 5000,
                target = mediaListSel,
                message = new MToolsListSelectorSelectNext(),
            };

            DMediaGuiContainer mediaRoller = new DMediaGuiContainer()
            {
                mediaList = mediaListSel,
                eventHandlers = new DToolsList(new DustData[] { 
                    actThumbActivator,
                })
            };

            DToolsSchedulerTask taskIntTimeout = new DToolsSchedulerTask()
            {
                type = EToolsSchedulerTaskType.Countdown,
                waitMsec = 10000,
            };

            DToolsActionMessage timeoutResetInteract = new DToolsActionMessage()
            {
                id = new DustIdentifier(EGuiComponentMessage.Interacted.ToString()),
                message = new MToolsSchedulerTaskMessagesCountdownReset(),
                target = taskIntTimeout,
            };

            DToolsActionMessage timeoutResetDisplay = new DToolsActionMessage()
            {
                id = new DustIdentifier(EGuiComponentMessage.Displayed.ToString()),
                message = new MToolsSchedulerTaskMessagesCountdownReset(),
                target = taskIntTimeout,
            };

            adHtmlPanel.eventHandlers = new DToolsList(new DustData[] { timeoutResetInteract });

            DToolsList schedTasks = new DToolsList(new DustData[] { taskIntTimeout });


            DGuiTable viewDefault = new DGuiTable()
            {
                eventHandlers = new DToolsList(new DustData[] { 
                    new DToolsActionMessage() { 
                        id = new DustIdentifier(EGuiComponentMessage.Displayed.ToString()),
                        target = schedTasks,
                        message = new MToolsListAdd(taskRoller) {}
                    },
                    new DToolsActionMessage() { 
                        id = new DustIdentifier(EGuiComponentMessage.Hidden.ToString()),
                        target = schedTasks,
                        message = new MToolsListRemove(taskRoller) {}
                    },
                }),

                layout = new DGuiTableLayout { borderStyle = TableLayoutPanelCellBorderStyle.Single },
                cols = new DToolsList(new DustData[] { 
                    new DGuiTableCol { style = new ColumnStyle(SizeType.Percent, 10F) },
                    new DGuiTableCol { style = new ColumnStyle(SizeType.Percent, 70F) },
                    new DGuiTableCol { style = new ColumnStyle(SizeType.Percent, 20F) },
                }),
                rows = new DToolsList(new DustData[] { 
                    new DGuiTableRow { style = new RowStyle(SizeType.Absolute, 38F) },
                    new DGuiTableRow { style = new RowStyle(SizeType.AutoSize) },
                }),
                cells = new DToolsList(new DustData[] { 
                    new DGuiTableCell { row = 0, col = 0, comp = new DGuiClock { timeFormat = "HH:mm:ss" } }, 
                    new DGuiTableCell { row = 0, col = 1, comp = rollingText1 },
                    new DGuiTableCell { row = 0, col = 2, comp = cmdLocalInfo },
                    new DGuiTableCell { row = 1, col = 0, colSpan = 2, comp = mediaRoller },
                    new DGuiTableCell { row = 1, col = 2, comp = localMenu },
                }),
            };

            DGuiTable viewInteractive = new DGuiTable()
            {
                eventHandlers = new DToolsList(new DustData[] { timeoutResetDisplay }),

                layout = new DGuiTableLayout { borderStyle = TableLayoutPanelCellBorderStyle.Single },
                cols = new DToolsList(new DustData[] { 
                    new DGuiTableCol { style = new ColumnStyle(SizeType.Percent, 10F) },
                    new DGuiTableCol { style = new ColumnStyle(SizeType.Percent, 80F) },
                    new DGuiTableCol { style = new ColumnStyle(SizeType.Percent, 10F) },
                }),
                rows = new DToolsList(new DustData[] { 
                    new DGuiTableRow { style = new RowStyle(SizeType.Absolute, 38F) },
                    new DGuiTableRow { style = new RowStyle(SizeType.AutoSize) },
                }),
                cells = new DToolsList(new DustData[] { 
                    new DGuiTableCell { row = 0, col = 0, comp = new DGuiClock { timeFormat = "HH:mm:ss" } }, 
                    new DGuiTableCell { row = 0, col = 1, comp = rollingText2 },
                    new DGuiTableCell { row = 0, col = 2, comp = cmdExit },
                    new DGuiTableCell { row = 1, col = 0, colSpan = 3, comp = adHtmlPanel },
                }),
            };

            DGuiXaml viewLocalInfo = new DGuiXaml {
                eventHandlers = new DToolsList(new DustData[] { timeoutResetDisplay }),
                xamlPath = "resources\\Page1.xaml" 
            };

            DGuiFlip main = new DGuiFlip()
            {
                content = new DToolsListSelector(viewDefault)
                {
                    list = new DToolsList(new DustData[] { viewDefault, viewInteractive, viewLocalInfo })
                }
            };

            actFlipDefault.target = actFlipInteractive.target = actFlipLocal.target = taskIntTimeout.target = (DustCompData)main.content;
            actFlipDefault.message = taskIntTimeout.message = new MToolsListSelectorSelect(viewDefault);
            actFlipInteractive.message = new MToolsListSelectorSelect(viewInteractive);
            actFlipLocal.message = new MToolsListSelectorSelect(viewLocalInfo);

            DToolsList listEventHandlers = new DToolsList(new DustData[] { 
                actThumbActivator, actFlipInteractive, actFlipDefault
            });

            PTSTerminal terminal = new PTSTerminal
            {
                environment = new DToolsEnvironment()
                {
                    scheduler = new DToolsScheduler()
                    {
                        tasks = schedTasks
                    },
                },
                ContentList = listAdContent,
                rootGui = main,
                eventHandlers = listEventHandlers,
            };

            return terminal;
        }

        PTSTerminal load(Serializer serializer, String file)
        {
            TextReader reader = new StreamReader(file);

            return (PTSTerminal)serializer.Deserialize(reader);
        }

        void save(DustData data, Serializer serializer, String file)
        {
            System.IO.TextWriter writer = new StreamWriter(file);
            serializer.Serialize(data, writer);
            writer.Close();
        }

        void displayGui(PTSTerminal data, Form mainForm)
        {
            Control ctrl = data.rootGui.getGuiCtrl();

            ctrl.Dock = DockStyle.Fill;
            ctrl.Visible = true;

            mainForm.Controls.Clear();

            mainForm.Controls.Add(ctrl);
        }

        //        public override void init(Object extEnv)
        public void init(Object extEnv)
        {
            SerializationContext ctx = new SerializationContext();
            ctx.ReferenceWritingType = SerializationContext.ReferenceOption.WriteIdentifier;
            PTSTerminal data;
            Serializer serializer = Serializer.GetSerializer(typeof(PTSTerminal), ctx);

            String file = "resources\\prog7.json.txt";

//            data = build();
//            save(data, serializer, file);
            data = load(serializer, file);

            ToolsEnvironment env = new ToolsEnvironment();
            env.init((DToolsEnvironment)data.environment, extEnv);
            env.start();

            displayGui(data, (Form)extEnv);
        }

        public void shutdown()
        {
            ((ToolsEnvironment)getEnv()).parallelShutdown();
        }
    }

}
