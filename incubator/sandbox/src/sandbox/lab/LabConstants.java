package sandbox.lab;

public interface LabConstants {
	public static interface StatefulComponent {
		void updateState();
	}
	public static interface DataAwareComponent extends StatefulComponent {
		void updateData();
	}
	
}
