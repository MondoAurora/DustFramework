using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Diagnostics;

namespace Dust.Core
{
    public interface DustFactory
    {
        DustComponentBase createComponentForData(DustCompData data);
    }

    public interface IDustEnvironment
    {
        void send(DustMessage msg, DustCompData source, DustCompData target);
        void listen(DustMessage.Filter filter, DustComponentBase processor);
        bool isEnvStopping();
        DustComponentBase createComponent(DustCompData data);
    }

    public abstract class DustEnvironment<DataType> : DustComponent<DataType>, IDustEnvironment where DataType : DustCompData
    {
        public static readonly Type FACTORY_TYPE = typeof(DustFactory);

        private object stopLock = new object();
        volatile bool stopping = false;

        public DustComponentBase createComponent(DustCompData data)
        {
            DustComponentBase comp = createComponentForData(data);

            comp.setup(data);

            componentAdded(comp);

            return comp;
        }

        protected DustComponentBase getComp(DustCompData data)
        {
            return data.getComp();
        }

        protected abstract void componentAdded(DustComponentBase comp);

        public virtual void init(DataType config, Object extEnv = null)
        {
            env = this;
            setup(config);
        }

        public bool isEnvStopping()
        {
            lock (stopLock)
            {
                return stopping;
            }
        }

        void flagStop()
        {
            lock (stopLock)
            {
                stopping = true;
            }
        }

        public Thread parallelShutdown()
        {
            Debug.WriteLine("Stopping is true");
            flagStop();

            Thread t = new Thread(new ThreadStart(end));
            t.Name = "Shutdown";
            t.Start();
            Thread.Sleep(50);

            return t;
        }

        public abstract void start();
        public virtual void end()
        {
            flagStop();
        }

//        protected abstract DustComponentBase createComponentForData(DustCompData data);

        public abstract void send(DustMessage msg, DustCompData source, DustCompData target);

        public abstract void listen(DustMessage.Filter filter, DustComponentBase processor);


        public abstract DustComponentBase createComponentForData(DustCompData data);
    }
}
