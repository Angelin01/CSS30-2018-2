from PyQt5 import QtCore, QtGui, QtWidgets
from enum import Enum
from location import Location
import requests


class FormType(Enum):
	PLANE_TICKET = 1
	LODGING = 2
	TRAVEL_PACKAGE = 3


class ItemList(object):
	_base_address = "http://localhost:8080/SampleApp"
	def __init__(self, formType: FormType):
		self._formType = formType
		self.form = QtWidgets.QWidget()

		# Set the appropriate URLs for the specified type
		if self._formType == FormType.PLANE_TICKET:
			self._update_items = self._update_PlaneTickets
			self._buy_address = "/api/agencia/buyplaneticket?&id={}&numTickets={}"
			
		elif self._formType == FormType.LODGING:
			self._update_items = self._update_Lodgings
			self._buy_address = "/api/agencia/buylodging?&id={}&numRooms={}"

		else:
			self._update_items = self._update_TravelPackages
			self._buy_address = "/api/agencia/buytravelpackage?&id={}&numPackages={}"

		self._filter_origin = None
		self._filter_destiny = None
		self._filter_max_price = None
		self._filter_departure_date = None
		self._filter_return_date = None
		self._filter_minimum_available = None


	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #
	# ADD COMMENTS TO METHODS ADD COMMENTS TO METHODS ADD COMMENTS TO METHODS ADD COMMENTS TO METHODS
	# ADD COMMENTS TO METHODS ADD COMMENTS TO METHODS ADD COMMENTS TO METHODS ADD COMMENTS TO METHODS
	# ADD COMMENTS TO METHODS ADD COMMENTS TO METHODS ADD COMMENTS TO METHODS ADD COMMENTS TO METHODS
	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #
	# =============================================================================================== #

	def setupUi(self):
		"""
		Mostly auto generated stuff by pyuic5 from the .ui files
		Not auto-generated stuff is isolated by whitespace and has comments explaining changes
		"""
		self.form.setObjectName("Form")
		self.form.resize(799, 598)
		self.verticalLayoutWidget = QtWidgets.QWidget(self.form)
		self.verticalLayoutWidget.setGeometry(QtCore.QRect(9, 9, 781, 581))
		self.verticalLayoutWidget.setObjectName("verticalLayoutWidget")
		self.mainLayout = QtWidgets.QVBoxLayout(self.verticalLayoutWidget)
		self.mainLayout.setContentsMargins(0, 0, 0, 0)
		self.mainLayout.setObjectName("mainLayout")
		self.label = QtWidgets.QLabel(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(16)
		self.label.setFont(font)
		self.label.setAlignment(QtCore.Qt.AlignCenter)
		self.label.setObjectName("label")
		self.mainLayout.addWidget(self.label)
		self.locationLayout = QtWidgets.QHBoxLayout()
		self.locationLayout.setObjectName("locationLayout")
		spacerItem = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.locationLayout.addItem(spacerItem)
		self.labelOrigin = QtWidgets.QLabel(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.labelOrigin.setFont(font)
		self.labelOrigin.setObjectName("labelOrigin")
		self.locationLayout.addWidget(self.labelOrigin)
		self.cmbOrigin = QtWidgets.QComboBox(self.verticalLayoutWidget)
		self.cmbOrigin.setMinimumSize(QtCore.QSize(120, 0))
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.cmbOrigin.setFont(font)
		self.cmbOrigin.setObjectName("cmbOrigin")
		self.locationLayout.addWidget(self.cmbOrigin)
		spacerItem1 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.locationLayout.addItem(spacerItem1)
		self.labelDestiny = QtWidgets.QLabel(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.labelDestiny.setFont(font)
		self.labelDestiny.setObjectName("labelDestiny")
		self.locationLayout.addWidget(self.labelDestiny)
		self.cmbDestiny = QtWidgets.QComboBox(self.verticalLayoutWidget)
		self.cmbDestiny.setMinimumSize(QtCore.QSize(120, 0))
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.cmbDestiny.setFont(font)
		self.cmbDestiny.setObjectName("cmbDestiny")
		self.locationLayout.addWidget(self.cmbDestiny)

		# Set Locations in the combo boxes
		# Also adds a "None" item
		self.cmbOrigin.addItem('Nenhum')
		self.cmbDestiny.addItem('Nenhum')
		self.cmbOrigin.addItems([l.value for l in Location])
		self.cmbDestiny.addItems([l.value for l in Location])

		spacerItem2 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.locationLayout.addItem(spacerItem2)
		self.mainLayout.addLayout(self.locationLayout)
		self.horizontalLayout_3 = QtWidgets.QHBoxLayout()
		self.horizontalLayout_3.setObjectName("horizontalLayout_3")
		spacerItem3 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.horizontalLayout_3.addItem(spacerItem3)
		self.labelMaxPrice = QtWidgets.QLabel(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.labelMaxPrice.setFont(font)
		self.labelMaxPrice.setObjectName("labelMaxPrice")
		self.horizontalLayout_3.addWidget(self.labelMaxPrice)
		self.editMaxPrice = QtWidgets.QLineEdit(self.verticalLayoutWidget)
		self.editMaxPrice.setObjectName("editMaxPrice")
		self.horizontalLayout_3.addWidget(self.editMaxPrice)
		spacerItem4 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.horizontalLayout_3.addItem(spacerItem4)
		self.labelMinAvailable = QtWidgets.QLabel(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.labelMinAvailable.setFont(font)
		self.labelMinAvailable.setObjectName("labelMinAvailable")
		self.horizontalLayout_3.addWidget(self.labelMinAvailable)
		self.editMinAvailable = QtWidgets.QLineEdit(self.verticalLayoutWidget)
		self.editMinAvailable.setObjectName("editMinAvailable")
		self.horizontalLayout_3.addWidget(self.editMinAvailable)
		spacerItem5 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.horizontalLayout_3.addItem(spacerItem5)
		self.mainLayout.addLayout(self.horizontalLayout_3)
		self.dateLayout = QtWidgets.QHBoxLayout()
		self.dateLayout.setObjectName("dateLayout")
		self.departureLayout = QtWidgets.QVBoxLayout()
		self.departureLayout.setObjectName("departureLayout")
		self.labelDeparture = QtWidgets.QLabel(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.labelDeparture.setFont(font)
		self.labelDeparture.setObjectName("labelDeparture")
		self.departureLayout.addWidget(self.labelDeparture)
		self.calendarDeparture = QtWidgets.QCalendarWidget(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		self.calendarDeparture.setFont(font)
		self.calendarDeparture.setObjectName("calendarDeparture")
		self.departureLayout.addWidget(self.calendarDeparture)
		self.dateLayout.addLayout(self.departureLayout)
		spacerItem6 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.dateLayout.addItem(spacerItem6)
		self.returnLayout = QtWidgets.QVBoxLayout()
		self.returnLayout.setObjectName("returnLayout")
		self.labelReturn = QtWidgets.QLabel(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.labelReturn.setFont(font)
		self.labelReturn.setObjectName("labelReturn")
		self.returnLayout.addWidget(self.labelReturn)
		self.calendarReturn = QtWidgets.QCalendarWidget(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		self.calendarReturn.setFont(font)
		self.calendarReturn.setObjectName("calendarReturn")
		self.returnLayout.addWidget(self.calendarReturn)
		self.dateLayout.addLayout(self.returnLayout)
		self.mainLayout.addLayout(self.dateLayout)
		self.tableItems = QtWidgets.QTableWidget(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(10)
		self.tableItems.setFont(font)
		self.tableItems.setColumnCount(7)
		self.tableItems.setObjectName("tableItems")
		self.tableItems.setRowCount(0)
		item = QtWidgets.QTableWidgetItem()
		self.tableItems.setHorizontalHeaderItem(0, item)
		item = QtWidgets.QTableWidgetItem()
		self.tableItems.setHorizontalHeaderItem(1, item)
		item = QtWidgets.QTableWidgetItem()
		self.tableItems.setHorizontalHeaderItem(2, item)
		item = QtWidgets.QTableWidgetItem()
		self.tableItems.setHorizontalHeaderItem(3, item)
		item = QtWidgets.QTableWidgetItem()
		self.tableItems.setHorizontalHeaderItem(4, item)
		item = QtWidgets.QTableWidgetItem()
		self.tableItems.setHorizontalHeaderItem(5, item)
		item = QtWidgets.QTableWidgetItem()
		self.tableItems.setHorizontalHeaderItem(6, item)

		# Sets the width for the columns in the table
		header = self.tableItems.horizontalHeader()
		header.setSectionResizeMode(0, QtWidgets.QHeaderView.ResizeToContents)
		header.setSectionResizeMode(3, QtWidgets.QHeaderView.ResizeToContents)
		header.setSectionResizeMode(4, QtWidgets.QHeaderView.ResizeToContents)
		header.setSectionResizeMode(6, QtWidgets.QHeaderView.Stretch)

		self.tableItems.horizontalHeader().setCascadingSectionResizes(True)
		self.tableItems.horizontalHeader().setStretchLastSection(True)
		self.tableItems.verticalHeader().setVisible(False)
		self.tableItems.verticalHeader().setStretchLastSection(False)

		# Configures the table to select a row on click
		self.tableItems.setSelectionBehavior(QtWidgets.QTableView.SelectRows)
		# Configures the table to not have editing
		self.tableItems.setEditTriggers(QtWidgets.QTableWidget.NoEditTriggers)

		self.mainLayout.addWidget(self.tableItems)
		self.buyLayout = QtWidgets.QHBoxLayout()
		self.buyLayout.setObjectName("buyLayout")
		spacerItem7 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.buyLayout.addItem(spacerItem7)
		self.labelNumberBuy = QtWidgets.QLabel(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.labelNumberBuy.setFont(font)
		self.labelNumberBuy.setObjectName("labelNumberBuy")
		self.buyLayout.addWidget(self.labelNumberBuy)
		self.editBuy = QtWidgets.QLineEdit(self.verticalLayoutWidget)
		self.editBuy.setMaximumSize(QtCore.QSize(40, 16777215))
		self.editBuy.setObjectName("editBuy")
		self.buyLayout.addWidget(self.editBuy)
		self.btnBuy = QtWidgets.QPushButton(self.verticalLayoutWidget)
		self.btnBuy.setMinimumSize(QtCore.QSize(100, 0))
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.btnBuy.setFont(font)
		self.btnBuy.setObjectName("btnBuy")
		self.buyLayout.addWidget(self.btnBuy)
		spacerItem8 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.buyLayout.addItem(spacerItem8)
		self.mainLayout.addLayout(self.buyLayout)

		# ======================================== #
		# Connects the buttons and other clickables and pressables to methods
		# Most are self explanatory just by reading the call
		# ======================================== #
		self.btnBuy.clicked.connect(self._buy)
		self.editBuy.returnPressed.connect(self._buy)
		self.editMinAvailable.textChanged[str].connect(self._update_minimum_available_filter)
		self.editMaxPrice.textChanged[str].connect(self._update_max_price_filter)
		self.calendarDeparture.clicked[QtCore.QDate].connect(self._update_departure_filter)
		self.calendarReturn.clicked[QtCore.QDate].connect(self._update_return_filter)
		self.cmbOrigin.currentTextChanged.connect(self._update_origin_filter)
		self.cmbDestiny.currentTextChanged.connect(self._update_destiny_filter)

		# Set the appropriate text for the specified type
		if self._formType == FormType.PLANE_TICKET:
			self._set_PlaneTicket()
		elif self._formType == FormType.LODGING:
			self._set_Lodging()
		elif self._formType == FormType.TRAVEL_PACKAGE:
			self._set_TravelPackage()

		QtCore.QMetaObject.connectSlotsByName(self.form)

		# Reads the list of available ITEMS from the api
		self._update_items()

	# ============================ #
	# Update Filters methods below
	# ============================ #

	def _update_minimum_available_filter(self, value):
		if value.isdigit() and int(value) > 0:
			self._filter_minimum_available = value
		else:
			self._filter_minimum_available = None

	def _update_max_price_filter(self, value):
		if value.isdigit() and int(value) > 0:
			self._filter_max_price = value
		else:
			self._filter_max_price = None

	def _update_origin_filter(self, value):
		if value == "Nenhum":
			self._filter_origin = None
		else:
			self._filter_origin = Location(value).name
		self._update_items(True)

	def _update_destiny_filter(self, value):
		if value == "Nenhum":
			self._filter_destiny = None
		else:
			self._filter_destiny = Location(value).name
		self._update_items(True)

	def _update_departure_filter(self, date):
		self._filter_departure_date = date.toString("yyyy-MM-dd")
		self._update_items(True)

	def _update_return_filter(self, date):
		self._filter_return_date = date.toString("yyyy-MM-dd")
		self._update_items(True)

	# ========================== #
	# Update Items methods below
	# ========================== #

	def _update_PlaneTickets(self, add_filters=False):
		"""
		Reads PlaneTickets from the API
		Uses _base_address + api_url + filters for a GET request
		"""
		url = self._base_address + "/api/agencia/planetickets"
		if add_filters:
			url += "?"
			if self._filter_origin:
				url += "origin=" + self._filter_origin + "&"
			if self._filter_destiny:
				url += "destiny=" + self._filter_destiny + "&"
			if self._filter_max_price:
				url += "maxPrice=" + self._filter_max_price + "&"
			if self._filter_departure_date:
				url += "departureDate=" + self._filter_departure_date + "&"
			if self._filter_return_date:
				url += "returnDate=" + self._filter_return_date + "&"
			if self._filter_minimum_available:
				url += "minimumSeats=" + self._filter_minimum_available + "&"

		try:
			plane_tickets = requests.get(url).json()
		except requests.exceptions.ConnectionError:
			# Exibir msg de erro
			return

		table_row = 0
		self.tableItems.setRowCount(len(plane_tickets))
		for plane in plane_tickets:
			self.tableItems.setItem(table_row, 0, QtWidgets.QTableWidgetItem(str(plane['id'])))
			self.tableItems.setItem(table_row, 1, QtWidgets.QTableWidgetItem(plane['origin']))
			self.tableItems.setItem(table_row, 2, QtWidgets.QTableWidgetItem(plane['destiny']))
			self.tableItems.setItem(table_row, 3, QtWidgets.QTableWidgetItem(plane['departureDate']))
			self.tableItems.setItem(table_row, 4, QtWidgets.QTableWidgetItem(plane['returnDate'] or ""))
			self.tableItems.setItem(table_row, 5, QtWidgets.QTableWidgetItem("R${},{:.2f}".format(int(plane['price'] / 100), plane['price'] % 100)))
			self.tableItems.setItem(table_row, 6, QtWidgets.QTableWidgetItem(plane['numSeats']))
			table_row += 1

	def _update_Lodgings(self, add_filters=False):
		"""
		Reads Lodgings from the API
		Uses _base_address + api_url + filters for a GET request
		"""
		url = self._base_address + "/api/agencia/lodgings"
		if add_filters:
			url += "?"
			if self._filter_origin:
				url += "location=" + self._filter_origin + "&"
			if self._filter_max_price:
				url += "maxPrice=" + self._filter_max_price + "&"
			if self._filter_departure_date:
				url += "checkIn=" + self._filter_departure_date + "&"
			if self._filter_return_date:
				url += "checkOut=" + self._filter_return_date + "&"
			if self._filter_minimum_available:
				url += "minimumRooms=" + self._filter_minimum_available + "&"

		try:
			lodgings = requests.get(url).json()
		except requests.exceptions.ConnectionError:
			# Exibir msg de erro
			return

		table_row = 0
		self.tableItems.setRowCount(len(lodgings))
		for lodging in lodgings:
			self.tableItems.setItem(table_row, 0, QtWidgets.QTableWidgetItem(str(lodging['id'])))
			self.tableItems.setItem(table_row, 1, QtWidgets.QTableWidgetItem(lodging['location']))
			self.tableItems.setItem(table_row, 3, QtWidgets.QTableWidgetItem(lodging['checkIn']))
			self.tableItems.setItem(table_row, 4, QtWidgets.QTableWidgetItem(lodging['checkOut']))
			self.tableItems.setItem(table_row, 5, QtWidgets.QTableWidgetItem("R${},{:.2f}".format(int(lodging['price']/100), lodging['price']%100)))
			self.tableItems.setItem(table_row, 6, QtWidgets.QTableWidgetItem(str(lodging['numRooms'])))
			table_row += 1
	
	def _update_TravelPackages(self, add_filters=False):
		"""
		Reads TravelPackages from the API
		Uses _base_address + api_url + filters for a GET request
		"""
		url = self._base_address + "/api/agencia/travelpackages"
		if add_filters:
			url += "?"
			if self._filter_origin:
				url += "origin=" + self._filter_origin + "&"
			if self._filter_destiny:
				url += "destiny=" + self._filter_destiny + "&"
			if self._filter_max_price:
				url += "maxPrice=" + self._filter_max_price + "&"
			if self._filter_departure_date:
				url += "departureDate=" + self._filter_departure_date + "&"
			if self._filter_return_date:
				url += "returnDate=" + self._filter_return_date + "&"
			if self._filter_minimum_available:
				url += "minimumAvailable=" + self._filter_minimum_available + "&"

		try:
			travel_packages = requests.get(url).json()
		except requests.exceptions.ConnectionError:
			# Exibir msg de erro
			return

		table_row = 0
		self.tableItems.setRowCount(len(travel_packages))
		for package in travel_packages:
			self.tableItems.setItem(table_row, 0, QtWidgets.QTableWidgetItem(str(package['id'])))
			self.tableItems.setItem(table_row, 1, QtWidgets.QTableWidgetItem(package['planeTicket']['origin']))
			self.tableItems.setItem(table_row, 2, QtWidgets.QTableWidgetItem(package['planeTicket']['destiny']))
			self.tableItems.setItem(table_row, 3, QtWidgets.QTableWidgetItem(package['planeTicket']['departureDate']))
			self.tableItems.setItem(table_row, 4, QtWidgets.QTableWidgetItem(package['planeTicket']['returnDate'] or ""))
			self.tableItems.setItem(table_row, 5, QtWidgets.QTableWidgetItem("R${},{:.2f}".format(int(package['price'] / 100), package['price'] % 100)))
			self.tableItems.setItem(table_row, 6, QtWidgets.QTableWidgetItem(package['numPackages']))
			table_row += 1

	# ====================== #
	# Buy Item methods below
	# ====================== #

	def _buy(self):
		# Check first if what is in the buy field is a positive number
		if not self.editBuy.text().isdigit() or int(self.editBuy.text()) <= 0:
			# Mostra uma janela de erro
			return

		# Check if there is a selected row
		if not self.tableItems.selectionModel().selectedRows():
			# Mostra uma janela de erro
			return

		id = self.tableItems.itemAt(self.tableItems.selectionModel().selectedRows()[0].row(), 0).text()
		amount = self.editBuy.text()

		response = get(self._base_address + self._buy_address.format(id, amount))
		if response.text == "true":
		# Mostra uma janela de sucesso
			pass
		else:
		# Mostra uma janela de fracasso
			pass
		self._update_items()


	# ========================== #
	# Translations methods below
	# ========================== #

	def _set_PlaneTicket(self):
		"""
		Modifies the UI for a PlaneTicket style
		Mostly just text modifications
		"""
		_translate = QtCore.QCoreApplication.translate
		self.form.setWindowTitle(_translate("Form", "Passagens"))
		self.label.setText(_translate("Form", "Passagens"))
		self.labelOrigin.setText(_translate("Form", "Origem:"))
		self.labelDestiny.setText(_translate("Form", "Destino:"))
		self.labelMaxPrice.setText(_translate("Form", "Preço Máximo:"))
		self.labelMinAvailable.setText(_translate("Form", "Mínimo Disponível:"))
		self.labelDeparture.setText(_translate("Form", "Data de Ida:"))
		self.labelReturn.setText(_translate("Form", "Data de Volta:"))
		item = self.tableItems.horizontalHeaderItem(0)
		item.setText(_translate("Form", "ID"))
		item = self.tableItems.horizontalHeaderItem(1)
		item.setText(_translate("Form", "Origem"))
		item = self.tableItems.horizontalHeaderItem(2)
		item.setText(_translate("Form", "Destino"))
		item = self.tableItems.horizontalHeaderItem(3)
		item.setText(_translate("Form", "Data de Ida"))
		item = self.tableItems.horizontalHeaderItem(4)
		item.setText(_translate("Form", "Data de Volta"))
		item = self.tableItems.horizontalHeaderItem(5)
		item.setText(_translate("Form", "Preço"))
		item = self.tableItems.horizontalHeaderItem(6)
		item.setText(_translate("Form", "Quantidade Disponível"))
		self.labelNumberBuy.setText(_translate("Form", "Quantidade para comprar:"))
		self.btnBuy.setText(_translate("Form", "Comprar"))

	def _set_Lodging(self):
		"""
		Modifies the UI for a Lodging style
		Hides the 'Destiny' fields in the combo box selection and on the table
		"""
		_translate = QtCore.QCoreApplication.translate
		self.form.setWindowTitle(_translate("Form", "Hospedagens"))
		self.label.setText(_translate("Form", "Hospedagens"))
		self.labelOrigin.setText(_translate("Form", "Localidade:"))
		self.labelDestiny.hide()
		self.cmbDestiny.setEnabled(False)
		self.cmbDestiny.hide()
		self.labelMaxPrice.setText(_translate("Form", "Preço Máximo:"))
		self.labelMinAvailable.setText(_translate("Form", "Mínimo Disponível:"))
		self.labelDeparture.setText(_translate("Form", "Data de Check In:"))
		self.labelReturn.setText(_translate("Form", "Data de Check Out:"))
		item = self.tableItems.horizontalHeaderItem(0)
		item.setText(_translate("Form", "ID"))
		self.tableItems.setColumnHidden(2, True)
		item = self.tableItems.horizontalHeaderItem(2)
		item.setText(_translate("Form", "Localidade"))
		item = self.tableItems.horizontalHeaderItem(3)
		item.setText(_translate("Form", "Data de Check In"))
		item = self.tableItems.horizontalHeaderItem(4)
		item.setText(_translate("Form", "Data de Check Out"))
		item = self.tableItems.horizontalHeaderItem(5)
		item.setText(_translate("Form", "Preço"))
		item = self.tableItems.horizontalHeaderItem(6)
		item.setText(_translate("Form", "Quantidade Disponível"))
		self.labelNumberBuy.setText(_translate("Form", "Quantidade para comprar:"))
		self.btnBuy.setText(_translate("Form", "Comprar"))

	def _set_TravelPackage(self):
		"""
		Modifies the UI for a TravelPackage style
		Pretty much identical to PlaneTicket (even calls that method) except for the titles
		"""
		self._set_PlaneTicket()
		_translate = QtCore.QCoreApplication.translate
		self.form.setWindowTitle(_translate("Form", "Pacotes"))
		self.label.setText(_translate("Form", "Pacotes"))


# For testing purposes only, thanks to the if wrap won't run when imported in another file
if __name__ == "__main__":
	import sys
	app = QtWidgets.QApplication(sys.argv)
	ui = ItemList(FormType.PLANE_TICKET)
	ui.setupUi()
	ui.form.show()
	sys.exit(app.exec_())
	
