using System;
using System.Collections.Generic;
using Dust.Core;
using System.Diagnostics;
using System.Reflection;

namespace Dust.Tools
{
    public class ToolsEnvironment : DustEnvironment<DToolsEnvironment>
    {
        public static readonly Type ENV_TYPE = typeof(ToolsEnvironment);

        private Dictionary<String, DustComponentBase> rootComponents;
        private ToolsEventQueue eventQueue;
        private ToolsScheduler scheduler;

        private List<DustFactory> factories = new List<DustFactory>();
        private List<String> assemblyPrefixes = new List<String>();

        class Log : DustComponentDefault
        {
            public override ProcessReturn process(DustMessage message, DustCompData source)
            {
                if (!(message.getMessageId() is EToolsSchedulerMessage))
                {
                    Console.WriteLine("Message received: {0}", message.getMessageId());
                }

                return ProcessReturn.Acknowledged;
            }
        };

        private DustComponentBase log = new Log();

        public ToolsEnvironment()
        {
            rootComponents = new Dictionary<String, DustComponentBase>();
            eventQueue = new ToolsEventQueue();
//            scheduler = new ToolsScheduler();

            addKnownNamespacePrefix("Dust");
        }

        protected void addKnownNamespacePrefix(String prefix)
        {
            assemblyPrefixes.Add(prefix);
        }

        protected override void componentAdded(DustComponentBase comp)
        {
        }

        public override void send(DustMessage msg, DustCompData source, DustCompData target)
        {
            eventQueue.send(msg, source, target);
        }

        public override void listen(DustMessage.Filter filter, DustComponentBase processor)
        {
            eventQueue.registerProcessor(filter, processor);
        }

        public override void init(DToolsEnvironment config, Object extEnv)
        {
            base.init(config, extEnv);

            Type tFactory = FACTORY_TYPE;
            Type tEnv = typeof ( ToolsEnvironment );

            foreach (Assembly b in AppDomain.CurrentDomain.GetAssemblies())
            {
                findFactory(b);
            }

            AppDomain.CurrentDomain.AssemblyLoad += new AssemblyLoadEventHandler(MyAssemblyLoadEventHandler);

            scheduler = (ToolsScheduler)getComp((DustCompData)config.scheduler);
        }

        void findFactory(Assembly b)
        {
            String aName = b.FullName;
            bool known = false;

            foreach (String prefix in assemblyPrefixes)
            {
                if (known = aName.StartsWith(prefix))
                {
                    break;
                }
            }
            if (known)
            {
                Debug.WriteLine("  {0}", b);
                foreach (Type t in b.GetExportedTypes())
                {
                    if (!t.IsAbstract && (!ENV_TYPE.IsAssignableFrom(t)) && FACTORY_TYPE.IsAssignableFrom(t))
                    {
                        factories.Add((DustFactory)b.CreateInstance(t.FullName));
                        Debug.WriteLine("     Factory added: {0}", t);
                    }
                }
            }
        }

        internal static DustComponentBase.ProcessReturn process(DustCompData processor, DustMessage msg, DustCompData source)
        {
            return ((ToolsEnvironment)getEnv()).getComp(processor).process(msg, source);
        }

        static void MyAssemblyLoadEventHandler(object sender, AssemblyLoadEventArgs args)
        {
            ((ToolsEnvironment)getEnv()).findFactory(args.LoadedAssembly);
        }

        public override void start()
        {
            eventQueue.registerProcessor(null, log);

            eventQueue.start();
            if (null != scheduler)
            {
                scheduler.start();
            }

            Debug.WriteLine("Started");
        }

        public override void end()
        {
            base.end();

            Debug.WriteLine("Stopping!");
            scheduler.stop();
            eventQueue.stop();
            Debug.WriteLine("Stop!");
        }

        public DustComponentBase getComponent(String strRef)
        {
            return rootComponents[strRef];
        }

        public override DustComponentBase createComponentForData(DustCompData data)
        {
            foreach (DustFactory fact in factories)
            {
                DustComponentBase comp;
                if (null != (comp = fact.createComponentForData(data)))
                {
                    return comp;
                }
            }
            return null;
        }
    }
}
