from PyQt5 import QtCore, QtGui, QtWidgets
from enum import Enum
from location import Location


class FormType(Enum):
	PLANE_TICKET = 1
	LODGING = 2
	TRAVEL_PACKAGE = 3


class ItemList(object):
	def __init__(self, formType: FormType):
		self._formType = formType
		self.form = QtWidgets.QWidget()

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
		spacerItem3 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.dateLayout.addItem(spacerItem3)
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
		spacerItem4 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.buyLayout.addItem(spacerItem4)
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
		spacerItem5 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.buyLayout.addItem(spacerItem5)
		self.mainLayout.addLayout(self.buyLayout)

		# ======================================== #
		# Connects the buttons and other clickables to methods
		# Most are self explanatory just by reading the call
		# ======================================== #
		self.btnBuy.clicked.connect(self.echo_row)
		self.calendarDeparture.clicked[QtCore.QDate].connect(self.echo_date)
		self.calendarReturn.clicked[QtCore.QDate].connect(self.echo_date)
		self.cmbOrigin.currentTextChanged.connect(self.echo_cmb)
		self.cmbDestiny.currentTextChanged.connect(self.echo_cmb)

		# Set the appropriate text for the specified type
		if self._formType == FormType.PLANE_TICKET:
			self._setPlaneTicket()
		elif self._formType == FormType.LODGING:
			self._setLodging()
		elif self._formType == FormType.TRAVEL_PACKAGE:
			self._setTravelPackage()


		QtCore.QMetaObject.connectSlotsByName(self.form)


		# fixme REMOVE WHEN I CAN READ THE STUFF
		# Test table
		for i in range(7):
			self.tableItems.insertRow(self.tableItems.rowCount())
			self.tableItems.setItem(self.tableItems.rowCount()-1, i, QtWidgets.QTableWidgetItem("TEXTO"))

	# fixme REMOVE
	def echo_cmb(self, value):
		if value == "Nenhum":
			print("")
		else:
			print(Location(value).name)

	# fixme REMOVE
	def echo_date(self, date):
		print(date.toString("yyyy-MM-dd"))

	# fixme REMOVE
	def echo_row(self):
		print(self.tableItems.selectionModel().selectedRows()[0].row())

	def _setPlaneTicket(self):
		"""
		Modifies the UI for a PlaneTicket style
		Mostly just text modifications
		"""
		_translate = QtCore.QCoreApplication.translate
		self.form.setWindowTitle(_translate("Form", "Passagens"))
		self.label.setText(_translate("Form", "Passagens"))
		self.labelOrigin.setText(_translate("Form", "Origem:"))
		self.labelDestiny.setText(_translate("Form", "Destino:"))
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

	def _setLodging(self):
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
		self.labelDeparture.setText(_translate("Form", "Data de Check In:"))
		self.labelReturn.setText(_translate("Form", "Data de Check Out:"))
		item = self.tableItems.horizontalHeaderItem(0)
		item.setText(_translate("Form", "ID"))
		self.tableItems.setColumnHidden(1, True)
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

	def _setTravelPackage(self):
		"""
		Modifies the UI for a TravelPackage style
		Pretty much identical to PlaneTicket (even calls that method) except for the titles
		"""
		self._setPlaneTicket()
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