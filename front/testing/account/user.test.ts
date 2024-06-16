import {MeComponent} from "../../src/app/components/me/me.component";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {expect} from "@jest/globals";
import {MatSnackBar, MatSnackBarModule} from "@angular/material/snack-bar";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {SessionService} from "../../src/app/services/session.service";
import {By} from "@angular/platform-browser";
import {DebugElement, NgZone} from "@angular/core";
import {UserService} from "../../src/app/services/user.service";
import {of} from "rxjs";
import {Router} from "@angular/router";
import {RouterTestingModule} from "@angular/router/testing";
import {AppComponent} from "../../src/app/app.component";

/**
 * Test du composant MeComponent
 */
describe('Component: MeComponent', () => {

    // Déclaration des variables
    let component: MeComponent;
    let fixture: ComponentFixture<MeComponent>;
    let debugElement: DebugElement;
    let router: Router;
    let userService: UserService;
    let sessionService: SessionService;
    let ngZone: NgZone;
    let matSnackBar: MatSnackBar;

    // Mock de la session
    const mockSessionService = {
        sessionInformation: {
            admin: true,
            id: 1
        }
    }

    // Avant chaque test on configure le composant et on récupère les services
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MeComponent],
            imports: [
                RouterTestingModule.withRoutes([
                    {path: "", component: AppComponent}
                ]),
                MatSnackBarModule,
                HttpClientModule,
                MatCardModule,
                MatFormFieldModule,
                MatSnackBarModule,
                MatIconModule,
                MatInputModule
            ],
            providers: [{provide: SessionService, useValue: mockSessionService}, UserService],
        })
            .compileComponents();

        // Création du composant et récupération des services et du router
        fixture = TestBed.createComponent(MeComponent);
        component = fixture.componentInstance;
        router = TestBed.inject(Router);
        userService = TestBed.inject(UserService);
        sessionService = TestBed.inject(SessionService);
        matSnackBar = TestBed.inject(MatSnackBar);
        ngZone = TestBed.inject(NgZone);

        // Initialisation du composant avec un utilisateur
        component.user = {
            id: 1,
            email: 'test@test.com',
            lastName: 'Test lastname',
            firstName: 'Test firstname',
            admin: true,
            password: 'Test pwd',
            createdAt: new Date(),
            updatedAt: new Date()
        }
        debugElement = fixture.debugElement;
        fixture.detectChanges();
    });

    // Test de la création du composant
    it('should display the user\'s name and email', () => {
        const userInfo = debugElement.queryAll(By.css('div[fxLayoutAlign="start center"]:not(.p2.w100) > p'));

        expect(userInfo[0].nativeElement.textContent).toBe('Name: Test firstname TEST LASTNAME');
        expect(userInfo[1].nativeElement.textContent).toBe('Email: test@test.com');
    });

    // Test de l'affichage des dates de création et de mise à jour
    it('should display the user\'s creation and update dates', () => {
        const userInfo = debugElement.queryAll(By.css('div.p2.w100 > p'));
        const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
        const formattedDate = new Intl.DateTimeFormat('en-US', options).format(component.user?.createdAt);

        expect(userInfo[0].nativeElement.textContent).toBe('Create at:  '+formattedDate);
        expect(userInfo[1].nativeElement.textContent).toBe('Last update:  '+formattedDate);
    });

    // Test de l'affichage du rôle de l'utilisateur
    it('should be an admin', () => {
        const userInfo = debugElement.query(By.css('p.my2'));
        expect(userInfo.nativeElement.textContent).toBe('You are admin');
    });

    // Test de la redirection vers la page de modification de l'utilisateur
    it('should go to the previous page', () => {
        const spyLocation = jest.spyOn(window.history, 'back');
        component.back();
        expect(spyLocation).toHaveBeenCalled();
    });

    // Test de la suppression de l'utilisateur
    it('should delete the user', () => {
        const spyDelete = jest.spyOn(component, 'delete');
        const spyUserService = jest.spyOn(userService, 'delete').mockReturnValue(of({}));
        const spySnackBar = jest.spyOn(matSnackBar, 'open').mockReturnThis();

        ngZone.run(() => {
            component.delete();
        });

        expect(spyDelete).toHaveBeenCalled();
        expect(spyUserService).toHaveBeenCalled();
        expect(spySnackBar).toHaveBeenCalledWith("Your account has been deleted !", 'Close', {duration: 3000});
    });
});