/**
 * this interface exists just to allow the WebserviceHelper to make callbacks.
 */

package utils;

public interface RequestReceiver {
	void requestFinished(String[] result) throws Exception;
}
