// CloudCoder - a web-based pedagogical programming environment
// Copyright (C) 2011, Jaime Spacco <jspacco@knox.edu>
// Copyright (C) 2011, David H. Hovemeyer <dhovemey@ycp.edu>
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.cloudcoder.app.client;

import org.cloudcoder.app.client.page.CloudCoderPage;
import org.cloudcoder.app.client.page.CoursesAndProblemsPage;
import org.cloudcoder.app.client.page.DevelopmentPage;
import org.cloudcoder.app.client.page.LoginPage;
import org.cloudcoder.app.shared.util.DefaultSubscriptionRegistrar;
import org.cloudcoder.app.shared.util.Publisher;
import org.cloudcoder.app.shared.util.Subscriber;
import org.cloudcoder.app.shared.util.SubscriptionRegistrar;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * CloudCoder entry point class.
 */
public class CloudCoder implements EntryPoint, Subscriber {
	private Session session;
	private SubscriptionRegistrar subscriptionRegistrar;
	private CloudCoderPage currentPage;
	
	private LayoutPanel layoutPanel;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		session = new Session();
		subscriptionRegistrar = new DefaultSubscriptionRegistrar();

		// Subscribe to all Session events
		session.subscribeToAll(Session.Event.values(), this, subscriptionRegistrar);

		layoutPanel = new LayoutPanel();
		layoutPanel.setSize("100%", "100%");
		RootPanel.get("cc-content").add(layoutPanel);
		
		changePage(new LoginPage());
	}
	
	private void changePage(CloudCoderPage page) {
		if (currentPage != null) {
			currentPage.deactivate();
			layoutPanel.remove(currentPage.getWidget());
		}
		page.setSession(session);
		// Create the page's Widget and add it to the DOM tree
		page.createWidget();
		layoutPanel.add(page.getWidget());
		// Now it is safe to activate the page
		page.activate();
		currentPage = page;
	}
	
	@Override
	public void eventOccurred(Object key, Publisher publisher, Object hint) {
		if (key == Session.Event.LOGIN) {
			changePage(new CoursesAndProblemsPage());
		} else if (key == Session.Event.PROBLEM_CHOSEN) {
			changePage(new DevelopmentPage());
		}
		
	}
}
