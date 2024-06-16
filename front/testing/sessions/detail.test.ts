import {DetailComponent} from "../../src/app/features/sessions/components/detail/detail.component";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {SessionService} from "../../src/app/services/session.service";
import {Session} from '../../src/app/features/sessions/interfaces/session.interface';
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientModule} from "@angular/common/http";
import {MatSnackBar, MatSnackBarModule} from "@angular/material/snack-bar";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {ReactiveFormsModule} from "@angular/forms";
import {expect} from "@jest/globals";
import {DebugElement, NgZone} from "@angular/core";
import {By} from "@angular/platform-browser";
import {of} from "rxjs";
import {Teacher} from "../../src/app/interfaces/teacher.interface";
import {SessionApiService} from "../../src/app/features/sessions/services/session-api.service";
import {Router} from "@angular/router";
import {ListComponent} from "../../src/app/features/sessions/components/list/list.component";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

/**
 * Test du composant DetailComponent
 */
describe('Component: DetailComponent', () => {
    // Déclaration des variables
    let component: DetailComponent;
    let fixture: ComponentFixture<DetailComponent>;
    let service: SessionService;
    let serviceApi: SessionApiService;
    let ngZone: NgZone;
    let debugElement: DebugElement;
    let router: Router;
    let spyRouter: jest.SpyInstance;
    let matSnackBar: MatSnackBar;
    let httpMock: HttpTestingController;

    // Mock de la session
    const mockSessionService = {
        sessionInformation: {
            admin: true,
            id: 1
        }
    }

    // Mock des données de la session
    const session: Session = {
        id: 1,
        name: 'Test Name',
        description: 'Test Description',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
    }

    // Mock des données de l'enseignant
    const teacher: Teacher = {
        id: 1,
        lastName: 'Test Lastname',
        firstName: 'Test Firstname',
        createdAt: new Date(),
        updatedAt: new Date()
    }

    // Avant chaque test on configure le composant et on récupère les services
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [
                RouterTestingModule.withRoutes([
                    {path: "sessions", component: ListComponent}
                ]),
                HttpClientModule,
                HttpClientTestingModule,
                MatSnackBarModule,
                ReactiveFormsModule,
                MatCardModule,
                MatIconModule
            ],
            declarations: [DetailComponent],
            providers: [{provide: SessionService, useValue: mockSessionService}, {provide: SessionApiService}],
        })
            .compileComponents();

        // Création du composant et récupération des services et du router
        service = TestBed.inject(SessionService);
        serviceApi = TestBed.inject(SessionApiService);
        fixture = TestBed.createComponent(DetailComponent);
        router = TestBed.inject(Router);
        httpMock = TestBed.inject(HttpTestingController);
        matSnackBar = TestBed.inject(MatSnackBar);
        spyRouter = jest.spyOn(router, 'navigate');
        component = fixture.componentInstance;
        component.session = session;
        component.teacher = teacher;
        component.sessionId = String(session.id);
        ngZone = TestBed.inject(NgZone);
        debugElement = fixture.debugElement;
        fixture.detectChanges();
    });

    // Test de l'existance du composant
    it('should have a session', () => {
        component.isAdmin = service.sessionInformation!.admin;
        expect(component.session).toBeDefined();
    });

    // Test de la possibilité de retourner en arrière
    it('should go back', () => {
        const spyBack = jest.spyOn(window.history, 'back').mockReturnValue(undefined);

        ngZone.run(() => {
            component.back();
        });

        expect(spyBack).toHaveBeenCalled();
    });

    // Test de la suppression de la session
    it('should delete successfully', () => {
        const spyDelete = jest.spyOn(serviceApi, 'delete').mockReturnValue(of({}));
        const spyMatSnackBar = jest.spyOn(matSnackBar, 'open').mockReturnThis();

        ngZone.run(() => {
            component.delete();
        });

        expect(spyDelete).toHaveBeenCalledWith(String(session.id));
        expect(spyMatSnackBar).toHaveBeenCalled();
        expect(spyRouter).toHaveBeenCalledWith(['sessions']);
    });

    // Test de la participation à la session
    it('should display the delete button', () => {
        const button: DebugElement = debugElement.query(By.css('button[color="warn"]'));

        expect(button).toBeDefined();
        expect(button.nativeElement.textContent).toContain('Delete');
    });

    // Test de l'affichage du nom de la session
    it('should display the session\'s name', () => {
        const name = debugElement.query(By.css('h1'));

        expect(name).toBeDefined();
        expect(name.nativeElement.textContent).toContain(session.name);
    });

    // Test de l'affichage du nom de l'enseignant
    it('should display the teacher\'s name', () => {
        const teacher = debugElement.query(By.css('.ml3'));
        expect(teacher).toBeDefined();
        expect(teacher).not.toBe(null);
    });

    // Test de l'affichage des dates et des participants de la session
    it('should display the session\'s dates and attendees', () => {
        const sessionInfo = debugElement.queryAll(By.css('.my2 > div[fxLayoutAlign="start center"]'));
        const options: Intl.DateTimeFormatOptions = {year: 'numeric', month: 'long', day: 'numeric'};
        const formattedDate = new Intl.DateTimeFormat('en-US', options).format(component.session!.date);

        expect(sessionInfo).toBeDefined();
        expect(sessionInfo[0].nativeElement.textContent).toContain('0 attendees');
        expect(sessionInfo[1].nativeElement.textContent).toContain(formattedDate);
    });

    // Test de l'affichage de la description de la session
    it('should display the session\'s description', () => {
        const description = debugElement.query(By.css('mat-card-content > div:not([fxLayout="row"])'));

        expect(description).toBeDefined();
        expect(description.nativeElement.textContent).toContain('Description: ' + session.description);
    });

    it('should display the session\'s creation and update dates', () => {
        const sessionDates = debugElement.queryAll(By.css('div[fxLayoutAlign="space-between center"].my2 > div:not([fxLayoutAlign="start center"])'));

        const options: Intl.DateTimeFormatOptions = {year: 'numeric', month: 'long', day: 'numeric'};
        const formattedDate = new Intl.DateTimeFormat('en-US', options).format(component.session!.createdAt);

        expect(sessionDates).toBeDefined();
        expect(sessionDates[0].nativeElement.textContent).toContain('Create at:  ' + formattedDate);
        expect(sessionDates[1].nativeElement.textContent).toContain('Last update:  ' + formattedDate);
    });

    // Test de la participation à la session
    it('should participate successfully', () => {
        const spyParticipate = jest.spyOn(serviceApi, 'participate').mockReturnValue(of(void {}));
        const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession').mockImplementation(() => {
            component.session = session;
            return of(session);
        });

        component.participate();

        expect(spyParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
        expect(fetchSessionSpy).toHaveBeenCalled();
        expect(fetchSessionSpy).toReturn();
        expect(component.session?.name).toBe('Test Name');
    });

    // Test de la non-participation à la session
    it('should unParticipate successfully', () => {
        const spyUnParticipate = jest.spyOn(serviceApi, 'unParticipate').mockReturnValue(of(void {}));
        const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession').mockImplementation(() => {
            component.session = session;
            return of(session); // Mock the fetchSession return value
        });

        component.unParticipate();

        expect(spyUnParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
        expect(fetchSessionSpy).toHaveBeenCalled();
        expect(component.session?.name).toBe('Test Name');

        // Mocker la réponse de fetchSession pour vérifier que la session a bien été mise à jour
        const req = httpMock.expectOne(`api/session/${session.id}`);
        expect(req.request.method).toBe('GET');
        req.flush(session); // Provide a mock response
    });
});
